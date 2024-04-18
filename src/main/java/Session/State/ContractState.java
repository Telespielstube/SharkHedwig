package Session.State;

import Message.Message;
import Message.Messageable;
import ProtocolRole.ProtocolRole;
import Session.Session;
import ProtocolRole.State.ProtocolState;

import java.util.Optional;

public class ContractState implements SessionState {
    private final Session session;
    private boolean isComplete;

    public ContractState(Session session) {
        this.session = session;
        this.isComplete = false;
    }

    @Override
    public Optional<Message> handle(Messageable message, ProtocolRole protocolRole, String sender) {
        return protocolRole.getCurrentProtocolState().handle(message, sender);
    }

    @Override
    public void nextState() {
        this.session.setSessionState(this.session.getNoSessionState());
    }

    @Override
    public void resetState() {
        this.session.setSessionState(this.session.getNoSessionState());
    }
}
