package Message.Contract;

import java.util.UUID;
import DeliveryContract.*;
import Message.MessageFlag;

public class ContractDocument extends AbstractContract {

    private final DeliveryContract deliveryContract;

    public ContractDocument(UUID uuid, MessageFlag messageFlag, long timestamp, DeliveryContract deliveryContract) {
        this.deliveryContract = deliveryContract;
    }

    public MessageFlag getMessageFlag() {
        return this.messageFlag;
    }

    public void setMessageFlag(MessageFlag messageFlag) {
        this.messageFlag = messageFlag;
    }

    @Override
    public Object getContent() {
        return null;
    }

    public DeliveryContract getDeliveryContract() {
        return this.deliveryContract;
    }

}
