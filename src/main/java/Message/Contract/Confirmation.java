package Message.Contract;

import DeliveryContract.ShippingLabel;
import DeliveryContract.TransitRecord;
import Message.MessageFlag;

import java.util.UUID;

public class Confirmation extends AbstractContract {

    private boolean confirm = false;

    public Confirmation(UUID uuid, MessageFlag messageFlag, long timestamp, boolean confirm) {
        this.uuid = uuid;
        this.messageFlag = messageFlag;
        this.timestamp = timestamp;
        this.confirm = confirm;
    }
    public MessageFlag getMessageFlag() {
        return this.messageFlag;
    }

    public void setMessageFlag(MessageFlag messageFlag) {
        this.messageFlag = messageFlag;
    }
}
