package Message.Identification;

import Message.MessageFlag;

import java.security.SecureRandom;
import java.util.UUID;

public class Response extends AbstractIdentification {

    private byte[] encryptedNumber;
    private byte[] decryptedNumber;

    public Response(UUID uuid, long timestamp) {
        this.uuid = uuid;
        this.timestamp = timestamp;
    }

    /**
     * Contrstructor for a response to a response message. The second response message does not contain an
     * encryptedNumber attribute anymore.
     *
     * @param uuid
     * @param decryptedNumber
     * @param messageFlag
     * @param timestamp
     */
    public Response(UUID uuid, byte[] decryptedNumber, MessageFlag messageFlag, long timestamp) {
        this.uuid = uuid;
        this.decryptedNumber = decryptedNumber;
        this.messageFlag = messageFlag;
        this.timestamp = timestamp;
    }

    /**
     * Constructor for a response to a challenge message.
     *
     * @param uuid
     * @param encryptedNumber
     * @param decryptedNumber
     * @param messageFlag
     * @param timestamp
     */
    public Response(UUID uuid, byte[] encryptedNumber, byte[] decryptedNumber, MessageFlag messageFlag, long timestamp) {
        this.uuid = uuid;
        this.encryptedNumber = encryptedNumber;
        this.decryptedNumber = decryptedNumber;
        this.messageFlag = messageFlag;
        this.timestamp = timestamp;
    }

    public byte[] getEncryptedNumber() {
        return this.encryptedNumber;
    }
    public void setEncryptedNumber(byte[] responseNumber) {
        this.encryptedNumber = responseNumber;
    }

    public byte[] getDecryptedNumber() {
        return this.decryptedNumber;
    }
    public void setDecryptedNumber(byte[] responseNumber) {
        this.decryptedNumber = decryptedNumber;
    }
    public MessageFlag getMessageFlag() {
        return this.messageFlag;
    }

    public void setMessageFlag(MessageFlag messageFlag) {
        this.messageFlag = messageFlag;
    }
}
