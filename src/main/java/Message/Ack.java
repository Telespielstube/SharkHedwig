package Message;

import java.util.UUID;

/**
 * This message object signals the confirmation of the receipt or processing of data.
 */
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
