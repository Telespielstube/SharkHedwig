package DeliveryContract;

public class DeliveryContract implements IDeliveryContract {

    private ShippingLabel shippingLabel;
    private TransitRecord transitRecord;

    public DeliveryContract() {}

    public DeliveryContract(ShippingLabel shippingLabel, TransitRecord transitRecord) {
        this.shippingLabel = shippingLabel;
        this.transitRecord = transitRecord;
    }

    public ShippingLabel getShippingLabel() {
        return shippingLabel;
    }

    public TransitRecord getTransitRecord() {
        return transitRecord;
    }
}
