package Message.Contract;

import Message.MessageFlag;
import java.util.UUID;

public class PickUp extends AbstractContract {

    private byte[] signedTransitEntry;
    public PickUp() {}

    public PickUp(UUID uuid, MessageFlag messageFlag, long timestamp, byte[] signedTransitEntry) {
        this.signedTransitEntry = signedTransitEntry;
    }

    public MessageFlag getMessageFlag() {
        return this.messageFlag;
    }

    public void setMessageFlag(MessageFlag messageFlag) {
        this.messageFlag = messageFlag;
    }

    @Override
    public PickUp getContent() {
        return this;
    }

    public byte[] getSignedTransitRecord() {
        return this.signedTransitEntry;

    }
}
