package Message.Contract;

import DeliveryContract.DeliveryContract;
import Message.Message;
import Message.MessageFlag;

import java.util.UUID;

public class Affirm extends Message {

    private final DeliveryContract deliveryContract;

    public Affirm(UUID uuid, MessageFlag messageFlag, long timestamp, DeliveryContract deliveryContract) {
        super(uuid, messageFlag, timestamp);
        this.deliveryContract = deliveryContract;
    }

    public DeliveryContract getDeliveryContract() {
        return this.deliveryContract;
    }
}
