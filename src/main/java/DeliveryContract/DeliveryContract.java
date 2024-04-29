package DeliveryContract;

import Location.Locationable;
import Misc.Utilities;
import Setup.AppConstant;

import java.util.Observable;

public class DeliveryContract extends Observable implements Contractable, Cloneable {

    private TransitRecord transitRecord;
    private ShippingLabel shippingLabel;
    private boolean isCreated;

    public DeliveryContract(String receiver, ShippingLabel shippingLabel, Locationable geoSpatial) {
        this.shippingLabel = shippingLabel;
        this.transitRecord = new TransitRecord();
        this.transitRecord.addEntry(new TransitEntry(this.transitRecord.countUp(),
                this.shippingLabel.getUUID(),
                AppConstant.PEER_NAME.toString(),
                receiver,
                geoSpatial.getCurrentLocation(),
                Utilities.createTimestamp(),
                null,
                null));
        setIsCreated(true);
        setChanged();
        notifyObservers(this);
    }

    public DeliveryContract(ShippingLabel shippingLabel, TransitRecord transitRecord) {
        this.shippingLabel = shippingLabel;
        this.transitRecord = transitRecord;
        setIsCreated(true);
        setChanged();
        notifyObservers(this);
    }

    public DeliveryContract() {}

    @Override
    public DeliveryContract get() {
        return this;
    }

    @Override
    public void setIsCreated(boolean isCreated) {
        this.isCreated = isCreated;
    }

    @Override
    public boolean isCreated() {
        return this.isCreated();
    }

    @Override
    public DeliveryContract clone()  {
        DeliveryContract deliveryContract;
        try {
            deliveryContract = (DeliveryContract) super.clone();
        } catch (CloneNotSupportedException e) {
            System.err.println(Utilities.createTimestamp() + ": " + e.getMessage() );
            throw new RuntimeException(e);
        }
        return deliveryContract;
    }

    /**
     * After the hand-over is complete the former transferor has to reset all DeliveryContract states to the initial states.
     */
    public void resetContractState() {
        clearChanged();
    }

    /**
     * Gets the ShippingLabel object.
     *
     * @return    ShippingLabel object.
     */
    public ShippingLabel getShippingLabel() {
        return this.shippingLabel.get();
    }

    /**
     * Gets the TransitRecord object.
     *
     * @return    TransitRecord object.
     */
    public TransitRecord getTransitRecord() {
        return this.transitRecord.get();
    }
}
