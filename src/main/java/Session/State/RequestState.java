package Session.State;

import DeliveryContract.DeliveryContract;
import DeliveryContract.ShippingLabel;
import Message.*;
import Session.SessionManager;
import ProtocolRole.*;

import java.util.Optional;

/**
 *
 */
public class RequestState implements SessionState {
    private final SessionManager sessionManager;

    public RequestState(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public Optional<Message> handle(Messageable message, ProtocolRole protocolRole, String sender) {
        return protocolRole.getCurrentProtocolState().handle(message, sender);
    }

    @Override
    public void nextState() {
        this.sessionManager.setSessionState(this.sessionManager.getContractState());
    }

    @Override
    public void resetState() {
        this.sessionManager.setSessionState(this.sessionManager.getNoSessionState());
    }
}
