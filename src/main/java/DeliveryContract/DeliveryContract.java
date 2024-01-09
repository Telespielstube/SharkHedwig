package DeliveryContract;

public class DeliveryContract implements IDeliveryContract {

    private final IDeliveryContract transitRecord;
    private final IDeliveryContract shippingLabel;
    private boolean isSent = false;
    private boolean isCreated = false;

    public DeliveryContract(IDeliveryContract shippingLabel, IDeliveryContract transitRecord) {
        this.shippingLabel = shippingLabel;
        this.transitRecord = transitRecord;
        this.isCreated = true;
    }

    @Override
    public DeliveryContract get() {
        return this;
    }

    @Override
    public boolean getIsCreated() {
        return this.isCreated;
    }

    @Override
    public void setIsCreated(boolean isCreated) {
        this.isCreated = isCreated;
    }

    /**
     * After the hand over is complete the former transferor has to reset all DeliveryContract states to the initial states.
     */
    public void resetContractState() {
        this.isCreated = false;
        this.setContractSent(false);
        this.shippingLabel.setIsCreated(false);
        this.transitRecord.setIsCreated(false);
    }

    /**
     * Gets the current state if contract is sent or not
     *
     * @return    true if sent and false if not.
     */
    public boolean getContractSent() {
        return this.isSent;
    }

    /**
     * Sets the state if contract is sent.
     *
     * @param isSent    true if sent, false if not.
     */
    public void setContractSent(boolean isSent) {
        this.isSent = isSent;
    }

    /**
     * Gets the ShippingLabel object.
     *
     * @return    ShippingLabel object.
     */
    public ShippingLabel getShippingLabel() {
        return (ShippingLabel) this.shippingLabel.get();
    }

    /**
     * Gets the TransitRecord object.
     *
     * @return    TransitRecord object.
     */
    public TransitRecord getTransitRecord() {
        return (TransitRecord) this.transitRecord.get();
    }

    /**
     * Formats the DeliveryContract object
     * @return
     */
    @Override
    public String toString() {
        return "DeliveryContract\n-----------------\n\n" + "Shipping label\n--------------\n" + getShippingLabel().toString() +
                "\n\n" + "Transit record\n--------------\n" + getTransitRecord().toString() + "\n";
    }
}
