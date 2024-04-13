package Session.State;

public interface SessionState {

    void isActive();
    void resetState();
    void nextState();
}
