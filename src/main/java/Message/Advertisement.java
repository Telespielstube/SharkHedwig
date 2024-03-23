package Message;

import java.io.Serializable;
import java.util.UUID;

/**
 * The advertisement messages is a small message that only has the task of offering the recipient a service.
 */
public class Advertisement extends Message {


    private boolean adTag;

    public Advertisement(UUID uuid, MessageFlag messageFlag, long timestamp, boolean adTag) {
        super(uuid, messageFlag, timestamp);
        this.adTag = adTag;
    }

    @Override
    public UUID getUUID() {
        return this.uuid;
    }

    @Override
    public MessageFlag getMessageFlag() {
        return this.messageFlag;
    }
    @Override
    public long getTimestamp() {
        return this.timestamp;
    }

    /**
     * Returns the advertisment tag.
     *
     * @return    true if tag is set, false if not.
     */
    public boolean getAdTag() {
        return this.adTag;
    }
}