package DeliveryContract;

import Message.IMessage;

public class DeliveryContract implements IDeliveryContract {

    public static boolean isCreated = false;

    private ShippingLabel shippingLabel;
    private TransitRecord transitRecord;

    public DeliveryContract() {
        isCreated = false;
    }

    public DeliveryContract(ShippingLabel shippingLabel, TransitRecord transitRecord) {
        this.shippingLabel = shippingLabel;
        this.transitRecord = transitRecord;
        isCreated = true;
    }

    public ShippingLabel getShippingLabel() {
        return shippingLabel;
    }

    public TransitRecord getTransitRecord() {
        return transitRecord;
    }

    public DeliveryContract getDeliveryContract() {
        return this;
    }
}
