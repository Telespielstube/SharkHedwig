package Session.Sessions;

import Message.IMessage;

import java.util.Optional;

public class Contract extends AbstractSession {

    @Override
    public Optional<Object> transferor(IMessage message) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> transferee(IMessage message) {
        return Optional.empty();
    }
}
