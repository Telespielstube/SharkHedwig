package Message.Contract;

import Message.Message;
import Message.MessageFlag;

import java.util.UUID;

public class Complete extends Message {

    public Complete(UUID uuid, MessageFlag messageFlag, long timestamp) {
        super(uuid, messageFlag, timestamp);
    }
}
