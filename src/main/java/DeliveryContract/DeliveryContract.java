package DeliveryContract;

import Location.IGeoSpatial;
import Misc.Utilities;
import Setup.AppConstant;

import java.io.Serializable;
import java.util.Observable;

public class DeliveryContract extends Observable implements IDeliveryContract {

    private TransitRecord transitRecord;
    private ShippingLabel shippingLabel;
    private boolean isCreated = false;
    private boolean isSent = false;

    public DeliveryContract(){}

    public DeliveryContract(String receiver, IGeoSpatial geoSpatial) {
        this.shippingLabel = getShippingLabel();
        this.transitRecord = new TransitRecord();
        this.transitRecord.addEntry(new TransitEntry(this.transitRecord.countUp(),
                this.shippingLabel.getUUID(),
                AppConstant.PEER_NAME.toString(),
                receiver,
                geoSpatial.getCurrentLocation(),
                Utilities.createTimestamp(),
                null,
                null));
        this.isCreated = true;
        setChanged();
        notifyObservers();
    }

    public DeliveryContract(ShippingLabel shippingLabel, TransitRecord transitRecord) {
        this.shippingLabel = shippingLabel;
        this.transitRecord = transitRecord;
        this.isCreated = true;
        setChanged();
        notifyObservers();
    }

    @Override
    public DeliveryContract get() {
        return this;
    }

    public boolean getIsCreated() {
        return this.isCreated;
    }

    public void setIsCreated(boolean isCreated) {
        this.isCreated = isCreated;
    }

    /**
     * After the hand-over is complete the former transferor has to reset all DeliveryContract states to the initial states.
     */
    public void resetContractState() {
        this.isCreated = false;
        this.isSent = false;
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
     *
     * @return    String representation of this object.
     */
    public String getString() {
        return "DeliveryContract\n-----------------\n\n" + "Shipping label\n--------------\n" + getShippingLabel().getString() +
                "\n\n" + "Transit record\n--------------\n" + getTransitRecord().getString() + "\n";
    }
}
