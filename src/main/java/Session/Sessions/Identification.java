package Session.Sessions;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

import Message.Identification.AckMessage;
import Message.Identification.Response;
import Misc.Utilities;
import Message.Identification.Challenge;
import Message.MessageFlag;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.pki.SharkPKIComponent;

public class Identification extends AbstractSession {

    private SharkPKIComponent sharkPKIComponent;
    private Response responseReply;
    private Challenge challenge;
    private AckMessage ackMessage;
    private String sender;
    private Cipher cipher;
    private byte[] secureNumber;


    public Identification(String sender, SharkPKIComponent sharkPKIComponent) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.sender = sender;
        this.sharkPKIComponent = sharkPKIComponent;
        this.cipher = Cipher.getInstance("RSA");
        this.messageList = new TreeMap<>();
        this.messageList = Collections.synchronizedSortedMap(messageList);// A HashMap to store sent and Received Messages with their timestamps as key and the Message as value.
    }

    public Challenge initSession() {
        try {
            this.challenge = new Challenge(this.challenge.createUUID(), Utilities.encryptRandomNumber(generateRandomNumber(), this.sharkPKIComponent.getPublicKey()),
                    MessageFlag.Challenge, Utilities.createTimestamp());
            this.messageList.put(this.challenge.getTimestamp(), this.challenge );
        } catch (ASAPSecurityException e) {
            throw new RuntimeException(e);
        }

        return this.challenge;
    }

    /**
     * Compares two decrypted secure random numbers to check if the message sender's identitiy is corrent.
     *
     * @param decryptedNumber    Received decrypted number. This is the securely generated Number from the Challenge object.
     * @return                   True if decrypted number matches the saved number.
     */
    public boolean compareDecryptedNumber(byte[] decryptedNumber) {
        return this.secureNumber.equals(decryptedNumber);
    }

    public Object unpackMessage(Object message) {
        Response response = null;
        if (((Response) message).getMessageFlag().equals(MessageFlag.Response)) {
            return handleResponse((Response) message);
        }
        if (((Response) message).getMessageFlag().equals(MessageFlag.ResponseRelpy)) {
            return handleResponseReply((Response) message);
        }
        if (((AckMessage) message).getMessageFlag().equals(MessageFlag.Ack)) {
            handleAckMessage((AckMessage) message);
        }
        return null;
    }

    /**
     * Processes the steaps after receiveing the Response message bt compares decryptedNumber to the encryptedNumber
     * in the snt Challenge Message objtect and decrypts the received encrypted number from the transferee.
     *
     * @param response    Received Response object.
     * @return            Response object.
     */
    public Response handleResponse(Response response) {
        if ( compareTimestamp(response.getTimestamp()) && compareDecryptedNumber(response.getDecryptedNumber()) ) {
            try {
                byte[] decryptedNumber = Utilities.decryptNumber(response.getEncryptedNumber(), this.sharkPKIComponent.getPrivateKey());
                this.responseReply = new Response(this.responseReply.createUUID(), decryptedNumber, MessageFlag.Response, Utilities.createTimestamp() );
                messageList.put(this.responseReply.getTimestamp(), this.responseReply);
            } catch (ASAPSecurityException e) {
                e.printStackTrace();
            }
        }
        return this.responseReply;
    }

    public AckMessage handleResponseReply(Response responseReply) {
        boolean isValid = false;
        if ( compareTimestamp(responseReply.getTimestamp()) && compareDecryptedNumber(responseReply.getDecryptedNumber()) ) {
            isValid = true;
            return new AckMessage(this.ackMessage.createUUID(), isValid, MessageFlag.Ack, Utilities.createTimestamp());
        }
        return null;
    }

    /**
     * Processes the steps after receiving the AckMessage object.
     */
    public void handleAckMessage(AckMessage ackMessage) {
        if ( compareTimestamp(ackMessage.getTimestamp()) ) {
            messageList.clear();
        }
    }
    /**
     * Generates a sceure random number. The SecureRandom class generates a number up to 128 bits.
     * Makes the chance of repeating smaller.
     *
     * @return    The secure random number converted to byte[] ready for encryption.
     */
    public byte[] generateRandomNumber() {
        SecureRandom secureRandom = new SecureRandom();
        BigInteger bigInt = BigInteger.valueOf(secureRandom.nextInt());
        this.secureNumber =  bigInt.toByteArray();
        return this.secureNumber;
    }
}
