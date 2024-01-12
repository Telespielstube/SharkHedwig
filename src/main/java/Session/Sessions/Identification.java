package Session.Sessions;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import javax.crypto.NoSuchPaddingException;

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

    public Identification(){}
    /**
     * Identification constructor
     *
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     */
    public Identification(SharkPKIComponent sharkPKIComponent) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.sharkPKIComponent = sharkPKIComponent;
        this.messageList = Collections.synchronizedSortedMap(new TreeMap<>());// A HashMap to store sent and Received Messages with their timestamps as key and the Message as value.
    }

    @Override
    public Optional<Object> transferor(IMessage message, String sender) {
        Optional<AbstractIdentification> messageObject = Optional.empty();

        switch(message.getMessageFlag()) {
            case Advertisement:
                messageObject = Optional.ofNullable(handleAdvertisement((Advertisement) message).orElse(null));
                break;
            case Response:
                messageObject = Optional.ofNullable((AbstractIdentification) handleResponse((Response) message).orElse(null));
                break;
            case Ack:
                messageObject = Optional.ofNullable(handleAckMessage((AckMessage) message).orElse(null));
                break;
            default:
                System.err.println("Message flag was incorrect: " + message.getMessageFlag());
                clearMessageList();
                break;
        }
        if (!messageObject.isPresent()) {
            clearMessageList();
        } else {
            addMessageToList(messageObject.get());
        }
        return Optional.of(messageObject);
    }

    @Override
    public Optional<Object> transferee(IMessage message, String sender) {
        Optional<AbstractIdentification> messageObject = Optional.empty();
        switch(message.getMessageFlag()) {
            case Challenge:
                messageObject = Optional.ofNullable(handleChallenge((Challenge) message).orElse(null));
                break;
            case ResponseReply:
                messageObject = Optional.ofNullable(handleResponseReply((Response) message).orElse(null));
                break;
            case Ready:
                messageObject = Optional.ofNullable(handleAckMessage((AckMessage) message).orElse(null));
                break;
            default:
                System.err.println("Message flag was incorrect: " + message.getMessageFlag());
                clearMessageList();
                break;
        }
        if (!messageObject.isPresent()) {
            clearMessageList();
        } else {
            addMessageToList(messageObject.get());
        }
        return Optional.of(messageObject);
    }

    /**
     * Handles a received Advertisement message.
     *
     * @param message    Advertisement message.
     *
     * @return           if message was valid a Challenge message, if not an empty Optional.
     */
    private Optional<Challenge> handleAdvertisement(Advertisement message) {
        this.secureNumber = generateRandomNumber();
        if (message.getMessageFlag().equals(MessageFlag.Advertisement) && message.getAdTag()) {
            try {
                return Optional.of(new Challenge(Utilities.createUUID(), MessageFlag.Challenge, Utilities.createTimestamp(),
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
            // Prevents infinite loop
            if (!response.getMessageFlag().equals(tmp.getMessageFlag())) {
                try {
                    byte[] decryptedNumber = ASAPCryptoAlgorithms.decryptAsymmetric(response.getEncryptedNumber(), this.sharkPKIComponent.getASAPKeyStore());
                    return Optional.of(new Response(Utilities.createUUID(), decryptedNumber, MessageFlag.Response, Utilities.createTimestamp()));
                } catch (ASAPSecurityException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
            return Optional.of(new AckMessage(Utilities.createUUID(), MessageFlag.Ack, Utilities.createTimestamp(), true));
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
    public Optional<AckMessage> handleAckMessage(AckMessage message)  {
        if (compareTimestamp(message.getTimestamp(), timeOffset) && message.getIsAck()) {
            return Optional.of(new AckMessage(Utilities.createUUID(), MessageFlag.Ready,
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
        if (message.getMessageFlag().equals(MessageFlag.Challenge)) {
            try {
                byte[] decryptedNumber = ASAPCryptoAlgorithms.decryptAsymmetric(message.getChallengeNumber(), this.sharkPKIComponent.getASAPKeyStore());
                return Optional.of(new Response(Utilities.createUUID(), MessageFlag.Response, Utilities.createTimestamp(), Utilities.encryptAsymmetric(generateRandomNumber(), this.sharkPKIComponent.getPublicKey()), decryptedNumber));
            } catch (ASAPSecurityException e) {
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
            return Optional.of(new AckMessage(Utilities.createUUID(), MessageFlag.Ack, Utilities.createTimestamp(), true));
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
        byte[] bytes = new byte[0];
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
