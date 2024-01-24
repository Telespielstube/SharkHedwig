package Message.Contract;

import Message.Message;
import Message.MessageFlag;

import java.util.UUID;

public class Ack extends Message {

    private boolean isAck = false;

    public Ack(UUID uuid, MessageFlag messageFlag, long timestamp, boolean isAck) {
        super(uuid, messageFlag, timestamp);
        this.isAck = isAck;
    }

    public boolean getIsAck() {
        return this.isAck;
    }
}
