package Session.Sessions;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import javax.crypto.NoSuchPaddingException;

import Message.Advertisement;
import Message.IMessage;
import Message.Identification.AbstractIdentification;
import Message.Identification.AckMessage;
import Message.Identification.Response;
import Misc.Utilities;
import Message.Identification.Challenge;
import Message.MessageFlag;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.pki.SharkPKIComponent;

public class Identification extends AbstractSession {

    private SharkPKIComponent sharkPKIComponent;
    private Response response;
    private Challenge challenge;
    private AckMessage ackMessage;
    private byte[] secureNumber;


    /**
     * Identification constructor
     *
     * @param sharkPKIComponent
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     */
    public Identification(SharkPKIComponent sharkPKIComponent) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.sharkPKIComponent = sharkPKIComponent;
        this.messageList = Collections.synchronizedSortedMap(new TreeMap<>());// A HashMap to store sent and Received Messages with their timestamps as key and the Message as value.
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

    public Object unpackMessage(IMessage message) {
        Optional<AbstractIdentification> messageObject = null;

        switch(message.getMessageFlag()) {
            case Advertisement:
                messageObject = Optional.of(handleAdvertisement((Advertisement) message).get());
                break;
            case Response:
                messageObject = Optional.of(handleResponse((Response) message).get()));
                break;
            case ResponseReply:
                messageObject = Optional.of(handleResponseReply((Response) message).get());
                break;
            case Ack:
                if (handleAckMessage((AckMessage) message)) {
                    break;
                }
            default:
                System.err.println("No message object ");
                break;
        }
        if (messageObject.isPresent()) {
            this.messageList.put(messageObject.get().getTimestamp(), messageObject);
        }
        return Optional.of(messageObject);
    }

    private Optional<Challenge> handleAdvertisement(Advertisement message) {
        if (message.getAdTag()) {
            try {
                return Optional.of(new Challenge(this.challenge.createUUID(), Utilities.encryptRandomNumber(generateRandomNumber(), this.sharkPKIComponent.getPublicKey()),
                        MessageFlag.Challenge, Utilities.createTimestamp()));
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
    private Optional<Response> handleResponse(Response response) {
        if ( compareTimestamp(response.getTimestamp()) && compareDecryptedNumber(response.getDecryptedNumber()) ) {
            try {
                byte[] decryptedNumber = Utilities.decryptNumber(response.getEncryptedNumber(), this.sharkPKIComponent.getPrivateKey());
                return Optional.of(new Response(this.response.createUUID(), decryptedNumber, MessageFlag.Response, Utilities.createTimestamp() ));
            } catch (ASAPSecurityException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return Optional.empty();
    }

    private Optional<AckMessage> handleResponseReply(Response responseReply) {
        if ( compareTimestamp(responseReply.getTimestamp()) && compareDecryptedNumber(responseReply.getDecryptedNumber()) ) {
            return Optional.of(new AckMessage(this.ackMessage.createUUID(), true, MessageFlag.Ack, Utilities.createTimestamp()));
        }
        return Optional.empty();
    }

    /**
     * Processes the steps after receiving the AckMessage object.
     *
     * @return
     */
    private boolean handleAckMessage(AckMessage ackMessage) {
        boolean complete = false;
        if ( compareTimestamp(ackMessage.getTimestamp()) && ackMessage.getIsAck() ) {
            messageList.clear();
            return complete = true;
        }
        return complete;
    }

    /**
     * Generates a sceure random number. The SecureRandom class generates a number up to 128 bits.
     * Makes the chance of repeating smaller.
     *
     * @return    The secure random number converted to byte[] ready for encryption.
     */
    private byte[] generateRandomNumber() {
        SecureRandom secureRandom = new SecureRandom();
        BigInteger bigInt = BigInteger.valueOf(secureRandom.nextInt());
        this.secureNumber =  bigInt.toByteArray();
        return this.secureNumber;
    }
}
