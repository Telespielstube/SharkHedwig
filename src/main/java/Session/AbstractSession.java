package Session;

import Message.IMessage;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

public abstract class AbstractSession implements ISession {

    protected boolean sessionComplete = false;
    protected int timeOffset = 5000;

    public abstract Optional<Object> transferor(IMessage message, String sender);

    public abstract Optional<Object> transferee(IMessage message, String sender);

    public boolean getSessionComplete(Object message) {
        return this.sessionComplete;
    }

}
