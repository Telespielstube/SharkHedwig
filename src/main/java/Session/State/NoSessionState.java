package Session.State;

import DeliveryContract.DeliveryContract;
import DeliveryContract.ShippingLabel;
import Message.Messageable;
import Message.Message;
import ProtocolRole.ProtocolRole;
import Session.SessionManager;
import java.util.Optional;

public class NoSessionState implements SessionState {
    private final SessionManager sessionManager;
    private ProtocolRole protocolRole;

    public NoSessionState(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public Optional<Message> handle(Messageable message, ProtocolRole protocolRole, ShippingLabel shippingLabel,
                                    DeliveryContract deliveryContract, String sender) {
        return protocolRole.getCurrentProtocolState().handle(message, shippingLabel, deliveryContract, sender);
    }

    @Override
    public void nextState() {
        this.sessionManager.setSessionState(this.sessionManager.getRequestState());
    }

    @Override
    public void resetState() {
        this.sessionManager.setSessionState(this.sessionManager.getNoSessionState());
    }

    @Override
    public String toString() {
        return "NoSession";
    }
}
