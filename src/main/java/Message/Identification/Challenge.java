package Message.Identification;

import Message.MessageFlag;

import java.security.SecureRandom;
import java.util.UUID;

public class Challenge extends AbstractIdentification {

    private byte[] challengeNumber;
    public Challenge(UUID uuid, long timestamp) {
        this.uuid = uuid;
        this.timestamp = timestamp;
    }

    public Challenge(UUID uuid, MessageFlag messageFlag, long timestamp, byte[] challengeNumber) {
        this.uuid = uuid;
        this.messageFlag = messageFlag;
        this.timestamp = timestamp;
        this.challengeNumber = challengeNumber;
    }

    public byte[] getChallengeNumber() {
        return this.challengeNumber;
    }

    public void setChallengeNumber(byte[] challengeNumber) {
        this.challengeNumber = challengeNumber;
    }


    public MessageFlag getMessageFlag() {
        return this.messageFlag;
    }

    public void setMessageFlag(MessageFlag messageFlag) {
        this.messageFlag = messageFlag;
    }
}
