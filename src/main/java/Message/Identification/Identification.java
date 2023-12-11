package Message.Identification;

import Message.IMessage;

import java.security.SecureRandom;
import java.util.UUID;

import static Misc.Constants.CHALLENGE_MESSAGE_FLAG;

public abstract class Identification implements IMessage {

    private UUID uuid;
    private SecureRandom challengeNumber;
    private int messageFlag = CHALLENGE_MESSAGE_FLAG;;
    private long timestamp;

    public UUID createUUID() {
        return UUID.randomUUID();
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public SecureRandom getChallengeNumber() {
        return this.challengeNumber;
    }

    public void setChallengeNumber(SecureRandom challengeNumber) {
        this.challengeNumber = challengeNumber;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    @Override
    public int getFlag() {
        return this.messageFlag;
    }

    public void setFlag(int flag) {
        this.messageFlag = flag;
    }
}
