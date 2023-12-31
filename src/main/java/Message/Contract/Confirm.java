package Message.Contract;

import DeliveryContract.DeliveryContract;
import Message.MessageFlag;

import java.util.UUID;

public class Confirm extends AbstractContract {

    private DeliveryContract deliveryContract;
    private boolean isConfirmed = false;

    public Confirm(UUID uuid, MessageFlag messageFlag, long timestamp, DeliveryContract deliveryContract, boolean isConfirmed) {
        this.deliveryContract = deliveryContract;
        this.isConfirmed = isConfirmed;
    }

    public MessageFlag getMessageFlag() {
        return this.messageFlag;
    }

    public void setMessageFlag(MessageFlag messageFlag) {
        this.messageFlag = messageFlag;
    }

    @Override
    public Confirm getContent() {
        return this;
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
