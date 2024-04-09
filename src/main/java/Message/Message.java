package Message;

import java.util.UUID;

public abstract class Message implements Messageable {

    protected UUID uuid;
    protected MessageFlag messageFlag;
    protected long timestamp;

    public Message(UUID uuid, MessageFlag messageFlag, long timestamp) {
        this.uuid = uuid;
        this.messageFlag = messageFlag;
        this.timestamp = timestamp;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public MessageFlag getMessageFlag() {
        return this.messageFlag;
    }
}
