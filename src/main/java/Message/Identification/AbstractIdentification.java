package Message.Identification;

import Message.IMessage;
import Message.MessageFlag;

import java.util.UUID;


public abstract class AbstractIdentification implements IMessage {

    protected UUID uuid;
    protected MessageFlag messageFlag;;
    protected long timestamp;

    public UUID getUUID() {
        return this.uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public abstract MessageFlag getMessageFlag();
    public abstract void setMessageFlag(MessageFlag messageFlag);

}
