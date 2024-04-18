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
    private ProtocolState currentProtocolState;
    private final ProtocolState transfereeState;
    private final ProtocolState transferorState;

    public ProtocolRole(Session session) {
        this.transferorState = new Transferor(this, session);
        this.transfereeState = new Transferee(this, session);
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

    public ProtocolState getTranfereeState() {
        return this.transfereeState;
    }
}
