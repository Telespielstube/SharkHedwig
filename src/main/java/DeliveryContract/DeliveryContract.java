package DeliveryContract;

public class DeliveryContract  {

    private final IContractComponent transitRecord;
    private final IContractComponent shippingLabel;
    private boolean isSent = false;

    public DeliveryContract(IContractComponent shippingLabel, IContractComponent transitRecord) {
        this.shippingLabel = shippingLabel;
        this.transitRecord = transitRecord;
        this.isSent = true;
    }

    public boolean getContractSent() {
        return this.isSent;
    }

    public void setContractSent(boolean isSent) {
        this.isSent = isSent;
    }

    public ShippingLabel getShippingLabel() {
        return (ShippingLabel) this.shippingLabel.get();
    }

    public TransitRecord getTransitRecord() {
        return (TransitRecord) this.transitRecord.get();
    }

    public DeliveryContract getDeliveryContract() {
        return this;
    }


}
