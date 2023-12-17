package Message.Identification;

import Message.MessageFlag;

import java.util.UUID;

public class AckMessage extends AbstractIdentification {

    private boolean isAck = false;

    public AckMessage() {}
    public AckMessage(UUID uuid, boolean isAck, MessageFlag messageFlag, long timestamp) {
        this.uuid = uuid;
        this.isAck = isAck;
        this.messageFlag = messageFlag;
        this.timestamp = timestamp;
    }

    public MessageFlag getMessageFlag() {
        return this.messageFlag;
    }

    public void setMessageFlag(MessageFlag messageFlag) {
        this.messageFlag = messageFlag;
    }

    public boolean getIsAck() {
        return this.isAck;
    }
    public void setIsAck(boolean isAck) {
        this.isAck = isAck;
    }
}
