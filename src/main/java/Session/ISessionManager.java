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
     * @return           An Optional MessageBuilder object, which can be null if the optionalMessage container is empty.
     */
     Optional<MessageBuilder> sessionHandling(IMessage message, String sender);

}
