package Message.Identification;

import Message.Message;
import Message.MessageFlag;

import java.util.UUID;

public class Challenge extends Message {

    private byte[] challengeNumber;

    public Challenge(UUID uuid, MessageFlag messageFlag, long timestamp, byte[] challengeNumber) {
        super(uuid, messageFlag, timestamp);
        this.uuid = uuid;
        this.messageFlag = messageFlag;
        this.timestamp = timestamp;
        this.challengeNumber = challengeNumber;
    }

    public byte[] getChallengeNumber() {
        return this.challengeNumber;
    }
}
