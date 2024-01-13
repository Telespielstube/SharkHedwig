package Session;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import javax.crypto.NoSuchPaddingException;

import Message.Message;
import Message.Identification.AckMessage;
import Message.Advertisement;
import Message.IMessage;
import Message.Identification.*;
import Misc.Utilities;
import Message.MessageFlag;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.asap.crypto.ASAPCryptoAlgorithms;
import net.sharksystem.pki.SharkPKIComponent;

public class Identification extends AbstractSession {

    private SharkPKIComponent sharkPKIComponent;
    private byte[] secureNumber;
    private Optional<Message> optionalMessage;

    public Identification(){}
    /**
     * Identification constructor
     *
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     */
    public Identification(SharkPKIComponent sharkPKIComponent) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.sharkPKIComponent = sharkPKIComponent;
        this.messageList = new TreeMap<>(); // A HashMap to store sent and Received Messages with their timestamps as key and the Message as value.
        this.optionalMessage = Optional.empty();
    }

    @Override
    public Optional<Object> transferor(IMessage message, String sender) {
        switch(message.getMessageFlag()) {
            case ADVERTISEMENT:
                this.optionalMessage = Optional.ofNullable((Message) handleAdvertisement((Advertisement) message).orElse(null));
                break;
            case RESPONSE:
                this.optionalMessage = Optional.ofNullable((Message) handleResponse((Response) message).orElse(null));
                break;
            case ACK:
                this.optionalMessage = Optional.ofNullable((Message) handleAckMessage((AckMessage) message).orElse(null));
                break;
            default:
                System.err.println("Message flag was incorrect: " + message.getMessageFlag());
                clearMessageList();
                break;
        }
        if (!this.optionalMessage.isPresent()) {
            clearMessageList();
        } else {
            addMessageToList(this.optionalMessage.get());
        }
        return Optional.of(this.optionalMessage.get());
    }

    @Override
    public Optional<Object> transferee(IMessage message, String sender) {
        switch(message.getMessageFlag()) {
            case CHALLENGE:
                this.optionalMessage = Optional.ofNullable((Message) handleChallenge((Challenge) message).orElse(null));
                break;
            case RESPONSE_REPLY:
                this.optionalMessage = Optional.ofNullable((Message) handleResponseReply((Response) message).orElse(null));
                break;
            case READY:
                this.optionalMessage = Optional.ofNullable((Message) handleAckMessage((AckMessage) message).orElse(null));
                break;
            default:
                System.err.println("Message flag was incorrect: " + message.getMessageFlag());
                clearMessageList();
                break;
        }
        if (!this.optionalMessage.isPresent()) {
            clearMessageList();
        } else {
            addMessageToList(this.optionalMessage.get());
        }
        return Optional.of(this.optionalMessage.get());
    }

    /**
     * Handles a received Advertisement message.
     *
     * @param message    Advertisement message.
     *
     * @return           if message was valid a Challenge message, if not an empty Optional.
     */
    private Optional<Challenge> handleAdvertisement(Advertisement message) {
        if (message.getAdTag()) {
            try {
                this.secureNumber = generateRandomNumber();
                return Optional.of(new Challenge(Utilities.createUUID(), MessageFlag.CHALLENGE, Utilities.createTimestamp(),
                        Utilities.encryptAsymmetric(this.secureNumber, this.sharkPKIComponent.getPublicKey())));
            } catch (ASAPSecurityException e) {
                throw new RuntimeException(e);
            }
        }
        return Optional.empty();
    }

    /**
     * Processes the steaps after receiveing the Response message bt compares decryptedNumber to the encryptedNumber
     * in the snt Challenge Message objtect and decrypts the received encrypted number from the transferee.
     *
     * @param response    Received Response object.
     * @return            Response object.
     */
    private Optional<Object> handleResponse(Response response) {
        Response tmp = (Response) this.getLastValueFromList();
        if ( compareTimestamp(response.getTimestamp(), timeOffset) && compareDecryptedNumber(response.getDecryptedNumber()) ) {
            try{
                byte[] decryptedNumber = ASAPCryptoAlgorithms.decryptAsymmetric(response.getEncryptedNumber(), this.sharkPKIComponent.getASAPKeyStore());
                return Optional.of(new Response(Utilities.createUUID(), MessageFlag.RESPONSE_REPLY, Utilities.createTimestamp(), decryptedNumber));
            } catch (ASAPSecurityException e) {
                System.err.println("Caught an ASAPSecurityException: " + e);
                throw new RuntimeException(e);
            }
        }
        return Optional.empty();
    }

    /**
     * An Acknowledgment massage to signal that the second secure code was decrypted successfully. It compares the timestamp
     * checks the flag and checks if the last TreeMap entry equals Ack.
     *
     * @param message    The received AckMessage object.
     * @return              An Optional AckMessage object if timestamp and ack flag are ok
     *                      or an empty Optional if it's not.
     */
    private Optional<AckMessage> handleAckMessage(AckMessage message)  {
        if (compareTimestamp(message.getTimestamp(), timeOffset) && message.getIsAck()) {
            return Optional.of(new AckMessage(Utilities.createUUID(), MessageFlag.READY,
                    Utilities.createTimestamp(), true));
        }
        return Optional.empty();
    }

    /**
     * After receiving the Challenge message the Transfere neds to create a Response message object.
     *
     * @param message    Challenge message object.
     *
     * @return           Response message object, or an empty Optional container.
     */

    private Optional<Response> handleChallenge(Challenge message) {
        if (message.getChallengeNumber().length > 0) {

            try {
                byte[] decryptedNumber = ASAPCryptoAlgorithms.decryptAsymmetric(message.getChallengeNumber(), this.sharkPKIComponent.getASAPKeyStore());
                return Optional.of(new Response(Utilities.createUUID(), MessageFlag.RESPONSE, Utilities.createTimestamp(), Utilities.encryptAsymmetric(generateRandomNumber(), this.sharkPKIComponent.getPublicKey()), decryptedNumber));
            } catch (ASAPSecurityException e) {
                System.err.println("Caught an ASAPSecurityException: " + e);
                throw new RuntimeException(e);
            }
        }
        return Optional.empty();
    }

    /**
     * The Challenge initiator has to solve a challenge from the Transferee too. This is the Cahllenge reply from the Transferor
     *
     * @param responseReply    ResponseRelpy message object.
     *
     * @return                 Optional AckMessage object or empty Optional.
     */
    private Optional<AckMessage> handleResponseReply(Response responseReply) {
        if ( compareTimestamp(responseReply.getTimestamp(), timeOffset) && compareDecryptedNumber(responseReply.getDecryptedNumber()) ) {
            return Optional.of(new AckMessage(Utilities.createUUID(), MessageFlag.ACK, Utilities.createTimestamp(), true));
        }
        return Optional.empty();
    }

    /**
     * Generates a sceure random number. The SecureRandom class generates a number up to 128 bits.
     * Makes the chance of repeating smaller.
     *
     * @return    The secure random number converted to byte[] ready for encryption.
     */
    private byte[] generateRandomNumber() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[10];
        secureRandom.nextBytes(bytes);
        return bytes;
    }

    /**
     * Compares two decrypted secure random numbers to check if the message sender's identitiy is corrent.
     *
     * @param decryptedNumber    Received decrypted number. This is the securely generated Number from the Challenge object.
     * @return                   True if decrypted number matches the saved number.
     */
    private boolean compareDecryptedNumber(byte[] decryptedNumber) {
        return Arrays.equals(this.secureNumber, decryptedNumber);
    }
}
