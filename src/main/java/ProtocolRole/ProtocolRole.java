package ProtocolRole;

import DeliveryContract.DeliveryContract;
import DeliveryContract.ShippingLabel;
import ProtocolRole.State.ProtocolState;
import ProtocolRole.State.Transferee;
import ProtocolRole.State.Transferor;
import Session.Session;
import net.sharksystem.pki.SharkPKIComponent;

/**
 * The class ProtocolRole maintains a state object (instance of a sublcass of ProtocolState) that represents the current
 * state of the ProtocolRole.
 */
public class ProtocolRole {
    private ProtocolState currentProtocolState;
    private final ProtocolState transfereeState;
    private final ProtocolState transferorState;

    public ProtocolRole(Session session, ShippingLabel shippingLabel, DeliveryContract deliveryContract, SharkPKIComponent sharkPKIComponent) {
        this.transferorState = new Transferor(this, session, shippingLabel, deliveryContract, sharkPKIComponent);
        this.transfereeState = new Transferee(this, session, shippingLabel, deliveryContract, sharkPKIComponent);
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
