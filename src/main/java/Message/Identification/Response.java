package Message.Identification;

import Message.Message;
import Message.MessageFlag;

import java.util.UUID;

public class Response extends Message {

    private byte[] encryptedNumber;
    private byte[] decryptedNumber;

    /**
     * Constructor for a response to a challenge message.
     *
     * @param uuid
     * @param encryptedNumber
     * @param decryptedNumber
     * @param messageFlag
     * @param timestamp
     */
    public Response(UUID uuid, MessageFlag messageFlag, long timestamp, byte[] encryptedNumber, byte[] decryptedNumber) {
        super(uuid, messageFlag, timestamp);
        this.uuid = uuid;
        this.messageFlag = messageFlag;
        this.timestamp = timestamp;
        this.encryptedNumber = encryptedNumber;
        this.decryptedNumber = decryptedNumber;
    }

    /**
     * Contrstructor for a response to a transferee response message. The second response message does not contain an
     * encryptedNumber attribute anymore.
     *
     * @param uuid
     * @param decryptedNumber
     * @param messageFlag
     * @param timestamp
     */
    public Response(UUID uuid, MessageFlag messageFlag, long timestamp, byte[] decryptedNumber) {
        super(uuid, messageFlag, timestamp);
        this.uuid = uuid;
        this.messageFlag = messageFlag;
        this.timestamp = timestamp;
        this.decryptedNumber = decryptedNumber;
    }

    public byte[] getEncryptedNumber() {
        return this.encryptedNumber;
    }

    public byte[] getDecryptedNumber() {
        return this.decryptedNumber;
    }
}
