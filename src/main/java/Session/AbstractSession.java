package Session;

import Message.IMessage;
import java.util.Optional;

public abstract class AbstractSession implements ISession {

    protected boolean sessionComplete = false;
    protected int timeOffset = 5000;

    public abstract Optional<Object> transferor(IMessage message, String sender);

    public abstract Optional<Object> transferee(IMessage message, String sender);

    public boolean getSessionComplete() {
        return this.sessionComplete;
    }

    public void setSessionComplete(boolean complete) {
        this.sessionComplete = complete;
    }
}
