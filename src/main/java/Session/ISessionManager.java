package Session;

import Message.IMessage;
import Message.MessageBuilder;

import java.util.Optional;

public interface ISessionManager {

    /**
     * The incoming messages are handled differently based on the current device state.
     * This methode handles the transferor role.
     *
     * @param message    Incoming generic type Message object.
     */
     Optional<MessageBuilder> sessionHandling(IMessage message, String sender);

}
