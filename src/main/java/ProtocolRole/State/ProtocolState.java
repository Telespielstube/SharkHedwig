package ProtocolRole.State;

import Message.Message;
import Message.Messageable;

import java.util.Optional;

/**
 * The initial state of the protocol is Transferee.
 * The state of the device changes to transferer if the user fills out a shipping label.
 * The second possibility of a change of state is when
 * the protocol hands over a carriage to a different device or destination.
 * This would change the state back to transferee.
 */
public interface ProtocolState {

    Optional<Message> handle(Messageable message, String sender);
    void changeRole();
}