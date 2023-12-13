package Message;

import java.util.UUID;

/**
 * The advertisement messages is a small message that only has the task of offering the recipient a service.
 */
public class Advertisement implements IMessage {

    private UUID uuid;
    private int messageFlag;
    private long timestamp;

    public Advertisement(UUID uuid, int messageFlag, long timestamp) {
        this.uuid = uuid;
        this.messageFlag = messageFlag;
        this.timestamp = timestamp;

    }
    @Override
    public UUID createUUID() {
        return null;
    }

    @Override
    public UUID getUuid() {
        return null;
    }

    @Override
    public int getMessageFlag() {
        return 0;
    }

    @Override
    public void setMessageFlag(int messageFlag) {

    }

    @Override
    public long getTimestamp() {
        return 0;
    }

    @Override
    public void setTimestamp(long currentTime) {

    }
}