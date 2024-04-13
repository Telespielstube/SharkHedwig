package Session.State;

import Session.Session;

public class RequestState implements SessionState {

    private final Session session;

    public RequestState(Session session) {
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
