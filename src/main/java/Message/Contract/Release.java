package Message.Contract;

import Message.Message;
import Message.MessageFlag;

import java.util.UUID;

/**
 * This message object signals the confirmation of the receipt or processing of data.
 */
public class Release extends Message {

    public Release(UUID uuid, MessageFlag messageFlag, long timestamp) {
        super(uuid, messageFlag, timestamp);
    }
}
