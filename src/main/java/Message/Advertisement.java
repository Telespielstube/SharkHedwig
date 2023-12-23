package Message;

import java.util.UUID;

/**
 * The advertisement messages is a small message that only has the task of offering the recipient a service.
 */
public class Advertisement implements IMessage {

    private UUID uuid;
    private boolean adTag;
    private MessageFlag messageFlag;
    private long timestamp;

    public Advertisement(UUID uuid, boolean adTag, MessageFlag messageFlag, long timestamp) {
        this.uuid = uuid;
        this.adTag = adTag;
        this.messageFlag = messageFlag;
        this.timestamp = timestamp;

    }

    @Override
    public UUID getUuid() {
        return null;
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
        return 0;
    }

    @Override
    public void setTimestamp(long currentTime) {

    }
}