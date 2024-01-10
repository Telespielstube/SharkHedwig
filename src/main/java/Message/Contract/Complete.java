package Message.Contract;

import Message.MessageFlag;

import java.util.UUID;

public class Complete extends AbstractContract {

    private boolean isComplete = false;

    public Complete(UUID uuid, MessageFlag messageFlag, long timestamp, boolean isComplete) {
        super(uuid, messageFlag, timestamp);
        this.isComplete = isComplete;
    }

    public Message.MessageFlag getMessageFlag() {
        return this.messageFlag;
    }

    public void setMessageFlag(MessageFlag messageFlag) {
        this.messageFlag = messageFlag;
    }

    public boolean getComplete() {
        return this.isComplete;
    }

    public void setComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }
}
