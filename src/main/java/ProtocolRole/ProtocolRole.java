package ProtocolRole;

import ProtocolRole.State.ProtocolState;
import ProtocolRole.State.Transferee;
import ProtocolRole.State.Transferor;
import Session.Session;

/**
 * The class ProtocolRole maintains a state object (instance of a sublcass of ProtocolState) that represents the current
 * state of the ProtocolRole.
 */
public class ProtocolRole {
    private ProtocolState currentState;
    private final ProtocolState transfereeState;
    private final ProtocolState transferorState;

    public ProtocolRole(Session session) {
        this.transferorState = new Transferor(this, session);
        this.transfereeState = new Transferee(this, session);
        this.currentState = this.transfereeState;
    }

    /**
     * The following methods are getters and setters to control the protocol states.
     */
    public ProtocolState getCurrentState() {
        return this.currentState;
    }

    public void setProtocolState(ProtocolState protocolState) {
        this.currentState = protocolState;
    }
    public ProtocolState getTransferorState() {
        return this.transferorState;
    }

    public ProtocolState getTranfereeState() {
        return this.transfereeState;
    }
}
