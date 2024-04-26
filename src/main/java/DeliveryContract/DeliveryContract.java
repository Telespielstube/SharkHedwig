package DeliveryContract;

import DeliveryContract.State.ContractState;
import DeliveryContract.State.DeliveryContractState;
import Location.Locationable;
import Misc.Utilities;
import Setup.AppConstant;

import java.util.Observable;

public class DeliveryContract extends Observable implements Contractable, Cloneable {

    private TransitRecord transitRecord;
    private ShippingLabel shippingLabel;
    private final ContractState deliveryContractState;
    private boolean isCreated = ContractState.NOT_CREATED.getState();

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
        this.isCreated = ContractState.CREATED.getState();
        setChanged();
        notifyObservers(this);
    }

    public DeliveryContract(ShippingLabel shippingLabel, TransitRecord transitRecord) {
        this.shippingLabel = shippingLabel;
        this.transitRecord = transitRecord;
        this.isCreated = ContractState.CREATED.getState();
        setChanged();
        notifyObservers(this);
    }

    public DeliveryContract() {
        this.deliveryContractState = new DeliveryContractState(this);
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
