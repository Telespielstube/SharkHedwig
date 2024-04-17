package Session.State;

import Message.Messageable;

import java.util.Optional;

public interface SessionState {

    Optional<Message.Message> handle(Messageable message, String sender);
    void nextState();
    void resetState();

}
