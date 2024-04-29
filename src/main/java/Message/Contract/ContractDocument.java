package Message.Contract;

import java.util.UUID;

import DeliveryContract.DeliveryContract;
import Message.Message;
import Message.MessageFlag;

public class ContractDocument extends Message {

    private final DeliveryContract deliveryContract;

    public ContractDocument(UUID uuid, MessageFlag messageFlag, long timestamp, DeliveryContract deliveryContract) {
        super(uuid, messageFlag, timestamp);
        this.deliveryContract = deliveryContract;
    }

    public DeliveryContract getDeliveryContract() {
        return this.deliveryContract;
    }

}
