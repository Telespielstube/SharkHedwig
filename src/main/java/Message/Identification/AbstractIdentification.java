package Message.Identification;

import Message.IMessage;

import java.security.SecureRandom;
import java.util.UUID;

import static Misc.Constants.CHALLENGE_MESSAGE_FLAG;

public abstract class AbstractIdentification implements IMessage {

    protected UUID uuid;
    protected int messageFlag = 0;;
    protected long timestamp;

    public UUID createUUID() {
        return UUID.randomUUID();
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setFlag(int flag) {
        this.messageFlag = flag;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public abstract int getMessageFlag();
    public abstract void setMessageFlag(int messageFlag);

}
