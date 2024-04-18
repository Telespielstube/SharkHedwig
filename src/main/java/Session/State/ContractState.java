package Session.State;

import DeliveryContract.DeliveryContract;
import DeliveryContract.ShippingLabel;
import Message.Message;
import Message.Messageable;
import ProtocolRole.ProtocolRole;
import Session.SessionManager;
import ProtocolRole.State.ProtocolState;

import java.util.Optional;

public class ContractState implements SessionState {
    private final SessionManager sessionManager;
    private boolean isComplete;

    public ContractState(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.isComplete = false;
    }

    @Override
    public Optional<Message> handle(Messageable message, ProtocolRole protocolRole, ShippingLabel shippingLabel,
                                    DeliveryContract deliveryContract, String sender) {
        return protocolRole.getCurrentProtocolState().handle(message, sender);
    }

    @Override
    public void nextState() {
        this.sessionManager.setSessionState(this.sessionManager.getNoSessionState());
    }

    @Override
    public void resetState() {
        this.sessionManager.setSessionState(this.sessionManager.getNoSessionState());
    }
}
