package Message.Contract;

import Message.Message;
import Message.MessageFlag;

import java.util.UUID;

public class Complete extends Message {

    private boolean isComplete = false;

    public Complete(UUID uuid, MessageFlag messageFlag, long timestamp, boolean isComplete) {
        super(uuid, messageFlag, timestamp);
        this.isComplete = isComplete;
    }

    public boolean getComplete() {
        return this.isComplete;
    }
}
