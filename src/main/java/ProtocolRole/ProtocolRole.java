package ProtocolRole;

import ProtocolRole.State.ProtocolState;
import ProtocolRole.State.Transferee;
import ProtocolRole.State.Transferor;

public class ProtocolRole {
    private ProtocolState currentState;
    private final ProtocolState tranfereeState;
    private final ProtocolState transferorState;

    public ProtocolRole() {
        this.transferorState = new Transferor(this);
        this.tranfereeState = new Transferee(this);
        this.currentState = this.tranfereeState;
    }

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
        return this.tranfereeState;
    }
}
