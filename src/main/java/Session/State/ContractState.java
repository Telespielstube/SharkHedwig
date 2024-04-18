package Session.State;

import Message.Message;
import Message.Messageable;
import Session.Session;
import ProtocolRole.State.ProtocolState;

import java.util.Optional;

public class ContractState implements SessionState {
    private final Session session;
    private boolean isComplete;

    public ContractState(Session session) {
        this.session = session;
        this.isComplete = false;
    }

//    /**
//     * If the previous session is completed the received contract message gets processed.
//     *
//     * @param message    Received contract message
//     */
//    @Override
//    public Optional<Message> handle() {
//        this.optionalMessage = protocolState.equals(ProtocolState.TRANSFEROR)
//                ? this.contract.transferor(message, null, this.sender)
//                : this.contract.transferee(message, this.sender);
//        if (this.optionalMessage.isPresent()) {
//            if (this.contract.getSessionComplete()) {
//                changeProtocolState();
//                resetAll();
//            }
//        } else {
//            resetAll();
//        }
//        return null;
//    }

    @Override
    public Optional<Message> handle(Messageable message, String sender) {
        return Optional.empty();
    }

    @Override
    public void nextState() {
        this.session.setSessionState(this.session.getNoSessionState());
    }

    @Override
    public void resetState() {
        this.session.setSessionState(this.session.getNoSessionState());
    }

    @Override
    public void stateComplete() {
        this.isComplete = true;
    }
}
