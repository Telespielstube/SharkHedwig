package Message.Request;

import Message.Identification.AbstractIdentification;
import Message.MessageFlag;

import java.util.UUID;

public class AckMessage extends AbstractIdentification {

    private boolean isAck = false;

    public AckMessage() {}
    public AckMessage(UUID uuid, MessageFlag messageFlag, long timestamp, boolean isAck) {
        this.uuid = uuid;
        this.messageFlag = messageFlag;
        this.timestamp = timestamp;
        this.isAck = isAck;
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
