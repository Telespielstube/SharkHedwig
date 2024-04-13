package Setup.State;

import Setup.ProtocolRole;

public class Transferee implements ProtocolState{

    private final ProtocolRole protocolRole;

    public Transferee(ProtocolRole protocolRole) {
        this.protocolRole = protocolRole;
    }

    @Override
    public boolean handle() {
        return false;
    }

    @Override
    public void changeRole() {
        this.protocolRole.setProtocolState(this.protocolRole.getTransferorState());
    }
}
