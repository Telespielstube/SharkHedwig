package Session.State;

import Message.Message;

import java.util.Optional;

public interface SessionState {

    Optional<Message> handle();
    void nextState();
    void resetState();
}
