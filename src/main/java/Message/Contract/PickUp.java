package Message.Contract;

import DeliveryContract.TransitRecord;
import Message.Message;
import Message.MessageFlag;
import java.util.UUID;

public class PickUp extends Message {

    private TransitRecord entryList;

    public PickUp(UUID uuid, MessageFlag messageFlag, long timestamp, TransitRecord entryList) {
        super(uuid, messageFlag, timestamp);
        this.entryList = entryList;
    }

    public TransitRecord getTransitRecord() {
        return this.entryList;

    }
}
