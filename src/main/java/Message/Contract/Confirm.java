package Message.Contract;

import DeliveryContract.DeliveryContract;
import Message.MessageFlag;

import java.util.UUID;

public class Confirm extends AbstractContract {

    private final DeliveryContract deliveryContract;
    private boolean isConfirmed = false;

    public Confirm(UUID uuid, MessageFlag messageFlag, long timestamp, DeliveryContract deliveryContract, boolean isConfirmed) {
        super(uuid, messageFlag, timestamp);
        this.deliveryContract = deliveryContract;
        this.isConfirmed = isConfirmed;
    }

    public MessageFlag getMessageFlag() {
        return this.messageFlag;
    }

    public void setMessageFlag(MessageFlag messageFlag) {
        this.messageFlag = messageFlag;
    }

    public DeliveryContract getDeliveryContract() {
        return this.deliveryContract;
    }

    public boolean getConfirmed() {
        return this.isConfirmed;
    }

    public void setConfirmed(boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }
}
