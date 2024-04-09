package DeliveryContract;

import Location.Locationable;
import Misc.Utilities;
import Setup.AppConstant;
import java.util.Observable;

public class DeliveryContract extends Observable implements Contractable {

    private TransitRecord transitRecord;
    private ShippingLabel shippingLabel;
    private boolean isCreated = ContractState.NOT_CREATED.getState();

    public DeliveryContract(){}

    public DeliveryContract(String receiver, Locationable geoSpatial) {
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
        this.isCreated = ContractState.CREATED.getState();
        setChanged();
        notifyObservers();
    }

    public DeliveryContract(ShippingLabel shippingLabel, TransitRecord transitRecord) {
        this.shippingLabel = shippingLabel;
        this.transitRecord = transitRecord;
        this.isCreated = ContractState.CREATED.getState();
        setChanged();
        notifyObservers();
    }

    @Override
    public DeliveryContract get() {
        return this;
    }

    @Override
    public boolean getIsCreated() {
        return this.isCreated;
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
