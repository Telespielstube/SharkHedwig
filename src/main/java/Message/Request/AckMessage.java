package Message.Request;

import Message.Message;
import Message.MessageFlag;

import java.util.UUID;

public class AckMessage extends Message {

    private boolean isAck = false;

    public AckMessage(UUID uuid, MessageFlag messageFlag, long timestamp, boolean isAck) {
        super(uuid, messageFlag, timestamp);
        this.isAck = isAck;
    }

    public boolean getIsAck() {
        return this.isAck;
    }
}
