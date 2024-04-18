package Message.NoSession;

import Message.Message;
import Message.MessageFlag;
import java.util.UUID;

/**
 * This class represents the message object that allows the transferee to send its offer.
 */
public class Solicitation extends Message {
    private boolean isSolicitated = false;

    public Solicitation(UUID uuid, MessageFlag messageFlag, long timestamp, boolean isSolicitated) {
        super(uuid, messageFlag, timestamp);
    }

    public boolean getSolicitate() {
        return this.isSolicitated;
    }
}

