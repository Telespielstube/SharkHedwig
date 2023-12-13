package Message.Identification;

import Message.MessageFlag;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

public class Challenge extends AbstractIdentification {

    private SecureRandom challengeNumber;
    public Challenge(UUID uuid, long timestamp) {
        this.uuid = uuid;
        this.timestamp = timestamp;
    }

    public Challenge(UUID uuid, SecureRandom challengeNumber, MessageFlag messageFlag, long timestamp) {
        this.uuid = uuid;
        this.challengeNumber = challengeNumber;
        this.messageFlag = messageFlag;
        this.timestamp = timestamp;
    }

    public SecureRandom getChallengeNumber() {
        return this.challengeNumber;
    }

    public void setChallengeNumber(SecureRandom challengeNumber) {
        this.challengeNumber = challengeNumber;
    }


    public int getMessageFlag() {
        return this.messageFlag.getFlag();
    }

    public void setMessageFlag(MessageFlag messageFlag) {
        this.messageFlag = messageFlag;
    }
}
