package Session.State;

import Message.Advertisement;
import Message.MessageFlag;
import Message.Solicitation;
import Misc.Utilities;
import Session.Session;
import Setup.ProtocolState;

import java.util.Optional;
import Message.*;

public class NoSessionState implements SessionState {
    private final Session session;

    public NoSessionState(Session session) {
        this.session = session;
    }

    @Override
    public Optional<Message> handle() {
        Optional<Message> optionalMessage = Optional.empty();
        if (this.protocolState.equals(ProtocolState.TRANSFEROR) && this.shippingLabelCreated) {
            optionalMessage = Optional.of(new Solicitation(Utilities.createUUID(), MessageFlag.SOLICITATION, Utilities.createTimestamp(), true));
        } else {
            optionalMessage = Optional.of(new Advertisement(Utilities.createUUID(), MessageFlag.ADVERTISEMENT, Utilities.createTimestamp(), true));
        }
        this.receivedMessageList.addMessageToList(this.optionalMessage.get());
        nextState();
        return optionalMessage;
    }

    @Override
    public void nextState() {
        this.session.setSessionState(session.getRequestState());
    }

    @Override
    public void resetState() {
        this.session.setSessionState(session.getNoSession());
    }
}
