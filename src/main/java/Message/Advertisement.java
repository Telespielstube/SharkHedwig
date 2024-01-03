package Message;

import java.io.Serializable;
import java.util.UUID;

/**
 * The advertisement messages is a small message that only has the task of offering the recipient a service.
 */
public class Advertisement implements IMessage, Serializable {

    private UUID uuid;
    private boolean adTag;
    private MessageFlag messageFlag;
    private long timestamp;

    public Advertisement(UUID uuid, MessageFlag messageFlag, long timestamp, boolean adTag) {
        this.uuid = uuid;
        this.messageFlag = messageFlag;
        this.timestamp = timestamp;
        this.adTag = adTag;
    }

    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    public boolean getAdTag() {
        return this.adTag;
    }

    public void setAdTag(boolean adTag) {
        this.adTag = adTag;
    }

    public MessageFlag getMessageFlag() {
        return this.messageFlag;
    }

    public void setMessageFlag(MessageFlag messageFlag) {
        this.messageFlag = messageFlag;
    }

    @Override
    public long getTimestamp() {
        return this.timestamp;
    }

    @Override
    public void setTimestamp(long currentTime) {

    }
}