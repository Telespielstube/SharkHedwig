package Session.State;

import Message.Message;
import Session.Session;
import Setup.State.ProtocolState;

import java.util.Optional;

public class RequestState implements SessionState {

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
    public Optional<Message> handle() {
        this.optionalMessage = this.protocolState.equals(ProtocolState.TRANSFEROR)
                ? this.request.transferor(message, this.shippingLabel, this.sender)
                : this.request.transferee(message, this.sender);
        if (this.optionalMessage.isPresent()) {
            if (this.request.getSessionComplete()) {
                this.sessionState = SessionState_tmp.REQUEST.nextState();
            }
        } else {
            resetAll();
        }
        return null;
    }

    @Override
    public void resetState() {

    }

    @Override
    public void nextState() {

    }
}
