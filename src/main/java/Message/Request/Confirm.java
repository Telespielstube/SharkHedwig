package Message.Request;

import Message.Message;
import Message.MessageFlag;

import java.util.UUID;

public class Confirm extends Message {

    public Confirm(UUID uuid, MessageFlag messageFlag, long timestamp) {
        super(uuid, messageFlag, timestamp);
    }
}
