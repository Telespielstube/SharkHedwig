package Session.State;

import Session.Session;

public class ContractState implements SessionState {
    private final Session session;

    public ContractState(Session session) {
        this.session = session;
    }

    @Override
    public void isActive() {

    }

    @Override
    public void resetState() {

    }

    @Override
    public void nextState() {

    }
}
