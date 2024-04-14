package Session.State;

import Message.*;
import Session.Session;
import ProtocolRole.*;

import java.util.Optional;

public class RequestState implements SessionState {

    private ProtocolRole protocolRole;
    private final Session session;

    /**
     * If the previous session is completed the received request message gets processed.
     *
     * @param message    Received request message
     */
    public RequestState(Session session) {
        this.session = session;
    }

    @Override
    public Optional<Message> handle(Messageable message, String sender) {
        Optional<Message> optionalMessage = Optional.empty();
        optionalMessage = this.protocolRole.getCurrentState().handle(message, sender);
        return optionalMessage;
    }

    @Override
    public void resetState() {

    }

    @Override
    public void nextState() {

    }
}
