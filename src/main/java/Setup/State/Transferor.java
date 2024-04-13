package Setup.State;

import Setup.ProtocolRole;

public class Transferor implements ProtocolState {

    private final ProtocolRole protocolRole;

    public Transferor(ProtocolRole protocolRole) {
        this.protocolRole = protocolRole;
    }
    @Override
    public boolean isActive() {
        return true;
    }
}
