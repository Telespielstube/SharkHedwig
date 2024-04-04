package Message.Request;

import Message.Message;
import Message.MessageFlag;

import java.util.UUID;

public class Confirm extends Message {

    private boolean isConfirmed = false;

    public Confirm(UUID uuid, MessageFlag messageFlag, long timestamp, boolean isConfirm) {
        super(uuid, messageFlag, timestamp);
        this.isConfirmed = isConfirm;
    }

    public boolean getConfirmed() {
        return this.isConfirmed;
    }
}
