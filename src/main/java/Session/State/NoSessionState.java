package Session.State;

import Message.Messageable;
import Message.Message;
import ProtocolRole.ProtocolRole;
import Session.Session;
import java.util.Optional;

public class NoSessionState implements SessionState {
    private final Session session;
    private ProtocolRole protocolRole;

    public NoSessionState(Session session) {
        this.session = session;
    }

    @Override
    public Optional<Message> handle(Messageable message, String sender) {
        return this.protocolRole.getCurrentState().handle(message, sender);
    }

    @Override
    public void nextState() {
        this.session.setSessionState(this.session.getRequestState());
    }

    @Override
    public void resetState() {
        this.session.setSessionState(this.session.getNoSessionState());
    }
}
