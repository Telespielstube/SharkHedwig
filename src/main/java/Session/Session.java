package Session;

import Session.State.NoSessionState;
import Session.State.RequestState;
import Session.State.ContractState;
import Session.State.SessionState;

/**
 * The class Session maintains a state object (instance of a subclass of SessionState) that represents the current
 * state of the Session.
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

    /**
     * The following methods are getters and setters to control the session states.
     */
    public SessionState getCurrentState() {
        return this.currentState;
    }

    public void setSessionState(SessionState sessionState) {
        this.currentState = sessionState;
    }

    public SessionState getNoSessionState() {
        return this.noSessionState;
    }

    public SessionState getRequestState() {
        return this.requestState;
    }

    public SessionState getContractState() {
        return this.contractState;
    }
}
