package Message;

import java.util.UUID;
import Message.IMessage;

import static Misc.Constants.ACK_MESSAGE_FLAG;

public class AckMessage implements IMessage {

    private UUID uuid;
    private int messageFlag = ACK_MESSAGE_FLAG;
    private long timestamp;
    private boolean isAck = false;

    public AckMessage(UUID uuid, int messageFlag, long timestamp, boolean isAck) {
        this.uuid = uuid;
        this.messageFlag = messageFlag;
        this.timestamp = timestamp;
        this.isAck = isAck;
    }
    public UUID createUUID() {
        return UUID.randomUUID();
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public int getMessageFlag() {
        return this.messageFlag;
    }

    @Override
    public void setMessageFlag(int messageFlag) {
        this.messageFlag = messageFlag;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean getAck() {
        return this.isAck;
    }

    public void setAck(boolean isAck) {
        this.isAck = isAck;
    }
}
