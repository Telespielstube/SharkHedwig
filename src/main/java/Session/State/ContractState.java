package Session.State;

import Message.Message;
import Session.Session;
import Setup.ProtocolState;

import java.util.Optional;

public class ContractState implements SessionState {
    private final Session session;

    public ContractState(Session session) {
        this.session = session;
    }

    /**
     * If the previous session is completed the received contract message gets processed.
     *
     * @param message    Received contract message
     */
    @Override
    public Optional<Message> handle() {
        this.optionalMessage = protocolState.equals(ProtocolState.TRANSFEROR)
                ? this.contract.transferor(message, null, this.sender)
                : this.contract.transferee(message, this.sender);
        if (this.optionalMessage.isPresent()) {
            if (this.contract.getSessionComplete()) {
                changeProtocolState();
                resetAll();
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
