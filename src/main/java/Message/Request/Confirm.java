package Message.Request;

import Message.MessageFlag;

import java.util.UUID;

public class Confirm extends AbstractRequest {

    private boolean isConfirmed = false;
    public Confirm() {}
    public Confirm(UUID uuid, MessageFlag messageFlag, long timestamp, boolean isConfirm) {
        this.uuid = uuid;
        this.messageFlag = messageFlag;
        this.timestamp = timestamp;
        this.isConfirmed = isConfirm;
    }

    public MessageFlag getMessageFlag() {
        return this.messageFlag;
    }

    public void setMessageFlag(MessageFlag messageFlag) {
        this.messageFlag = messageFlag;
    }

    public boolean getIsConfirmed() {
        return this.isConfirmed;
    }
    public void setIsConfirm(boolean isAck) {
        this.isConfirmed = isConfirmed;
    }
}
