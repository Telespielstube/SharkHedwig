package Setup;

import Setup.State.ProtocolState;
import Setup.State.Transferee;
import Setup.State.Transferor;

public class ProtocolRole {
    private final ProtocolState currentState;
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
    public ProtocolState getTransferorState() {
        return this.transferorState;
    }

    public ProtocolState getTranfereeState() {
        return this.tranfereeState;
    }
}
