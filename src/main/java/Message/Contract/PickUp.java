package Message.Contract;

import DeliveryContract.TransitRecord;
import Message.MessageFlag;
import java.util.UUID;

public class PickUp extends AbstractContract {

    private TransitRecord entryList;
    public PickUp() {}

    public PickUp(UUID uuid, MessageFlag messageFlag, long timestamp, TransitRecord entryList) {
        this.entryList = entryList;
    }

    public MessageFlag getMessageFlag() {
        return this.messageFlag;
    }

    public void setMessageFlag(MessageFlag messageFlag) {
        this.messageFlag = messageFlag;
    }

    public TransitRecord getTransitRecord() {
        return this.entryList;

    }
}
