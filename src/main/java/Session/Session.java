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

    public SessionState getCurrentState() {
        return this.currentState;
    }

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
}
