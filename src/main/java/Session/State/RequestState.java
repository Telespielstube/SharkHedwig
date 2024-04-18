package Session.State;

import Message.*;
import Session.Session;
import ProtocolRole.*;

import java.util.Optional;

public class RequestState implements SessionState {

    private ProtocolRole protocolRole;
    private final Session session;

    /**
     *
     */
    public RequestState(Session session) {
        this.session = session;
    }

    @Override
    public Optional<Message> handle(Messageable message, String sender) {
        return this.protocolRole.getCurrentState().handle(message, sender);
    }

    @Override
    public void nextState() {
        this.session.setSessionState(this.session.getContractState());
    }

    @Override
    public void resetState() {
        this.session.setSessionState(this.session.getNoSessionState());
    }
}
