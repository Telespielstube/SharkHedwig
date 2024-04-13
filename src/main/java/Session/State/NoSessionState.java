package Session.State;

import Session.Session;

public class NoSessionState implements SessionState {
    private final Session session;

    public NoSessionState(Session sessionStateManager) {
        this.session = sessionStateManager;
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
