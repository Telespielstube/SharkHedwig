package ProtocolRole;

import Battery.Battery;
import DeliveryContract.DeliveryContract;
import DeliveryContract.ShippingLabel;
import Location.GeoSpatial;
import ProtocolRole.State.ProtocolState;
import ProtocolRole.State.Transferee;
import ProtocolRole.State.Transferor;
import net.sharksystem.pki.SharkPKIComponent;

/**
 * The class ProtocolRole maintains a state object (instance of a sublcass of ProtocolState) that represents the current
 * state of the ProtocolRole.
 */
public class ProtocolRole {
    private ProtocolState currentProtocolState;
    private final ProtocolState transfereeState;
    private final ProtocolState transferorState;

    public ProtocolRole(ShippingLabel shippingLabel, DeliveryContract deliveryContract,
                        Battery battery, GeoSpatial geoSpatial, SharkPKIComponent sharkPKIComponent) {
        this.transferorState = new Transferor(this, shippingLabel, deliveryContract,
                sharkPKIComponent);
        this.transfereeState = new Transferee(this, shippingLabel, deliveryContract,
                battery, geoSpatial, sharkPKIComponent);
        this.currentProtocolState = this.transfereeState;
    }

    /**
     * The following methods are getters and setters to control the protocol states.
     */
    public ProtocolState getCurrentProtocolState() {
        return this.currentProtocolState;
    }

    public void setProtocolState(ProtocolState protocolState) {
        this.currentProtocolState = protocolState;
    }
    public ProtocolState getTransferorState() {
        return this.transferorState;
    }

    public ProtocolState getTransfereeState() {
        return this.transfereeState;
    }

    /**
     * Changes the current protocol role from transferor to transferee and vice versa.
     */
    public void changeRole() {
        if (getCurrentProtocolState().equals(getTransferorState())) {
            setProtocolState(getTransfereeState());
        } else {
            setProtocolState(getTransferorState());
        }
    }
}
