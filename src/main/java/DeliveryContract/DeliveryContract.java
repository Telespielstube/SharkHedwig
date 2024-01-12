package DeliveryContract;

import Location.IGeoSpatial;
import Misc.Utilities;
import Setup.AppConstant;

import java.io.Serializable;
import java.util.Observable;

public class DeliveryContract extends Observable implements IDeliveryContract, Serializable {

    private final TransitRecord transitRecord;
    private final ShippingLabel shippingLabel;
    private boolean isCreated = false;

    public DeliveryContract(String sender, IGeoSpatial geoSpatial) {
        this.shippingLabel = getShippingLabel();
        this.transitRecord = new TransitRecord();
        this.transitRecord.addEntry(new TransitEntry(this.transitRecord.countUp(), this.shippingLabel.getUUID(), AppConstant.PeerName.toString(),
                sender, geoSpatial.getCurrentLocation(), Utilities.createTimestamp(), null, null));
        setChanged();
        notifyObservers(this.isCreated);
    }

    public DeliveryContract(ShippingLabel shippingLabel, TransitRecord transitRecord){
        this.shippingLabel = shippingLabel;
        this.transitRecord = transitRecord;
        setChanged();
        notifyObservers(this.isCreated = true);
    }

    @Override
    public DeliveryContract get() {
        return this;
    }

    public void setIsCreated(boolean isCreated) {
        this.isCreated = isCreated;
    }

    public boolean getIsCreated() {
        return this.isCreated;
    }




    /**
     * After the hand over is complete the former transferor has to reset all DeliveryContract states to the initial states.
     */
//    public void resetContractState() {
//        this.isCreated = false;
//        this.setContractSent(false);
////        this.shippingLabel.setIsCreated(false);
////        this.transitRecord.setIsCreated(false);
//    }
//

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
