package Session.State;

import Message.Message;

import java.util.Optional;

public interface SessionState {

    Optional<Message.Message> handle(Message.Messageable message, String sender);
    void nextState();
    void resetState();
}
