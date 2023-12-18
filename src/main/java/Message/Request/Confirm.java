package Message.Request;

import Message.Identification.AbstractIdentification;
import Message.MessageFlag;

import java.util.UUID;

public class Confirm extends AbstractRequest {

    private boolean isConfirm = false;
    public Confirm() {}
    public Confirm(UUID uuid, MessageFlag messageFlag, long timestamp, boolean isConfirm) {
        this.uuid = uuid;
        this.messageFlag = messageFlag;
        this.timestamp = timestamp;
        this.isConfirm = isConfirm;
    }

    public MessageFlag getMessageFlag() {
        return this.messageFlag;
    }

    public void setMessageFlag(MessageFlag messageFlag) {
        this.messageFlag = messageFlag;
    }

    public boolean getIsAck() {
        return this.isConfirm;
    }
    public void setIsAck(boolean isAck) {
        this.isConfirm = isConfirm;
    }
}
