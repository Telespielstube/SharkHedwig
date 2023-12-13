package Message.Identification;

import Message.MessageFlag;

import java.security.SecureRandom;
import java.util.UUID;

public class Challenge extends AbstractIdentification {

    private int challengeNumber;
    public Challenge(UUID uuid, long timestamp) {
        this.uuid = uuid;
        this.timestamp = timestamp;
    }

    public Challenge(UUID uuid, int challengeNumber, MessageFlag messageFlag, long timestamp) {
        this.uuid = uuid;
        this.challengeNumber = challengeNumber;
        this.messageFlag = messageFlag;
        this.timestamp = timestamp;
    }

    public int getChallengeNumber() {
        return this.challengeNumber;
    }

    public void setChallengeNumber(int challengeNumber) {
        this.challengeNumber = challengeNumber;
    }


    public MessageFlag getMessageFlag() {
        return this.messageFlag;
    }

    public void setMessageFlag(MessageFlag messageFlag) {
        this.messageFlag = messageFlag;
    }
}
