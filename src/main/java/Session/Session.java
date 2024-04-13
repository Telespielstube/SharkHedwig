package Session;

import Session.State.NoSessionState;
import Session.State.RequestState;
import Session.State.ContractState;
import Session.State.SessionState;

/**
 * Interface for all protocol sessions.
 */
public class Session {
    private SessionState currentState;
    private final SessionState noSessionState;
    private final SessionState requestState;
    private final SessionState contractState;

    public Session() {
        this.noSessionState = new NoSessionState(this);
        this.requestState = new RequestState(this);
        this.contractState = new ContractState(this);
        this.currentState = this.noSessionState;
    }

    public SessionState getCurrentState() { return this.currentState; }

    public void setSessionState(SessionState sessionState) {
        this.currentState = sessionState;
    }

    public SessionState getNoSession() {
        return this.noSessionState;
    }

    public SessionState getRequestState() {
        return this.requestState;
    }

    public SessionState getContractState() {
        return this.contractState;
    }



//package Session;
//
//import DeliveryContract.ShippingLabel;
//import Message.Messageable;
//import Message.Message;
//
//import java.util.Optional;
//
///**
// * Interface for all protcol sessions.
// */
//public interface Session {
//    /**
//     * if protocol is in transferor state.
//     *
//     * @param message    Message object to be processed by transferor
//     *
//     * @return
//     */
//    Optional<Message> transferor(Messageable message, ShippingLabel shippingLabel, String sender);
//
//    /**
//     * If protocol is in transferee state.
//     *
//     * @param message
//     *
//     * @return
//     */
//    Optional<Message> transferee(Messageable message, String sender);
//
//    /**
//     * Returns the current state of the session.
//     *
//     * @return  boolean value of the session state.
//     */
//    boolean getSessionComplete();
//
//    /**
//     * If all messages of a session are exchanged the list needs to be checked if
//     * all messages are cleared out.
//     *
//     * @param message    Message object.
//     * @return           true if list is cleared.
//     */
//    void setSessionComplete(boolean complete);
}
