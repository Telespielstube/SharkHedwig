package Session.State;

import Message.Messageable;
import ProtocolRole.ProtocolRole;

import java.util.Optional;

public interface SessionState {

    Optional<Message.Message> handle(Messageable message, ProtocolRole protocolRole, String sender);
    void nextState();
    void resetState();
}
