package DeliveryContract;

public class DeliveryContract implements IDeliveryContract {

    private ShippingLabel shippingLabel;
    private TransitRecord transitRecord;

    public static boolean contractCreated = false;

    public DeliveryContract() {
        contractCreated = false;
    }

    public DeliveryContract(ShippingLabel shippingLabel, TransitRecord transitRecord) {
        this.shippingLabel = shippingLabel;
        this.transitRecord = transitRecord;
        contractCreated = true;
    }

    public ShippingLabel getShippingLabel() {
        return shippingLabel;
    }

    public TransitRecord getTransitRecord() {
        return transitRecord;
    }
}
