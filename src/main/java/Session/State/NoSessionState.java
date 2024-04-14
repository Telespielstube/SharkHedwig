package Session.State;

import Message.*;
import Misc.Utilities;
import ProtocolRole.ProtocolRole;
import Session.Session;
import ProtocolRole.State.ProtocolState;

import java.util.Optional;
import Message.*;

public class NoSessionState implements SessionState {

    private final Session session;
    private ProtocolRole protocolRole;

    public NoSessionState(Session session) {
        this.session = session;
    }

    @Override
    public Optional<Message> handle(Messageable message, String sender) {
        Optional<Message> optionalMessage = Optional.empty();
        optionalMessage = this.protocolRole.getCurrentState().handle(message, sender);
        return optionalMessage;
//        Optional<Message> optionalMessage;
//
//        if (this.protocolState.equals(ProtocolState.TRANSFEROR) && this.shippingLabelCreated) {
//            optionalMessage = Optional.of(new Solicitation(Utilities.createUUID(), MessageFlag.SOLICITATION, Utilities.createTimestamp(), true));
//        } else {
//            optionalMessage = Optional.of(new Advertisement(Utilities.createUUID(), MessageFlag.ADVERTISEMENT, Utilities.createTimestamp(), true));
//        }
//        this.receivedMessageList.addMessageToList(optionalMessage.get());
//        nextState();
//        return optionalMessage;
    }

    @Override
    public void nextState() {
        this.session.setSessionState(this.session.getRequestState());
    }

    @Override
    public void resetState() {
        this.session.setSessionState(this.session.getNoSession());
    }
}
