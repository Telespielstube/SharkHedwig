package Message.Identification;

import Message.IMessage;

import java.util.UUID;

public abstract class Identification implements IMessage {
    public UUID createUUID() {
        return UUID.randomUUID();
    }
}
