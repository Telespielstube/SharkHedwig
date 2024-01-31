package Message.Authentication;

import Message.Message;
import Message.MessageFlag;

import java.util.UUID;

public class Response extends Message {

    private byte[] challengeNumber;
    private byte[] decryptedNumber;

    /**
     * Constructor for a response to a challenge message.
     *
     * @param uuid
     * @param challengeNumber
     * @param decryptedNumber
     * @param messageFlag
     * @param timestamp
     */
    public Response(UUID uuid, MessageFlag messageFlag, long timestamp, byte[] challengeNumber, byte[] decryptedNumber) {
        super(uuid, messageFlag, timestamp);
        this.uuid = uuid;
        this.messageFlag = messageFlag;
        this.timestamp = timestamp;
        this.challengeNumber = challengeNumber;
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

    public byte[] getChallengeNumber() {
        return this.challengeNumber;
    }

    public byte[] getDecryptedNumber() {
        return this.decryptedNumber;
    }
}
