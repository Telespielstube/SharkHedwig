package Setup.State;

import Setup.ProtocolRole;

public class Transferor implements ProtocolState {

    private final ProtocolRole protocolRole;

    public Transferor(ProtocolRole protocolRole) {
        this.protocolRole = protocolRole;
    }

    @Override
    public void handle() {
        this.protocolRole.setProtocolState(this.protocolRole.getTransferorState());
    }

    @Override
    public void changeRole() {
        this.protocolRole.setProtocolState(this.protocolRole.getTranfereeState());
    }
}
