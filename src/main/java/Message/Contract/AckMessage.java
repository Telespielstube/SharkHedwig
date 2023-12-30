package Message.Contract;

import Message.MessageFlag;

import java.util.UUID;

public class AckMessage extends AbstractContract {

    private boolean isAck = false;

    public AckMessage() {}
    public AckMessage(UUID uuid, MessageFlag messageFlag, long timestamp, boolean isAck) {
        this.isAck = isAck;
    }

    public MessageFlag getMessageFlag() {
        return this.messageFlag;
    }

    public void setMessageFlag(MessageFlag messageFlag) {
        this.messageFlag = messageFlag;
    }

    @Override
    public Object getContent() {
        return null;
    }

    public boolean getIsAck() {
        return this.isAck;
    }
    public void setIsAck(boolean isAck) {
        this.isAck = isAck;
    }
}
