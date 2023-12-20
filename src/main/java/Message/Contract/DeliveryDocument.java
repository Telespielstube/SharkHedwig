package Message.Contract;

import java.util.UUID;
import DeliveryContract.*;
import Message.MessageFlag;

public class DeliveryDocument extends AbstractContract {

    private DeliveryContract deliveryContract;

    public DeliveryDocument() {}
    public DeliveryDocument(UUID uuid, MessageFlag messageFlag, long timestamp, DeliveryContract deliveryContract) {
        this.deliveryContract = deliveryContract;
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

}
