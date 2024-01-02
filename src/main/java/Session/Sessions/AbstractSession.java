package Session.Sessions;

import Message.IMessage;
import Message.Identification.AckMessage;
import Message.MessageFlag;
import Misc.Utilities;

import java.util.Collections;
import java.util.Optional;
import java.util.SortedMap;

public abstract class AbstractSession implements ISession {

    protected SortedMap<Long, Object> messageList;
    private final long timeOffset = 5000;
    private boolean sessionComplete = false;

    public abstract Optional<Object> transferor(IMessage message, String sender);

    public abstract Optional<Object> transferee(IMessage message, String sender);

    public boolean compareTimestamp(long timestamp) {
        if (timestamp - this.messageList.lastKey() < this.timeOffset) {
            return true;
        }
        return false;
    }

    public Object getLastValueFromList() {
        return this.messageList.get(this.messageList.lastKey());
    }

    @Override
    public boolean sessionComplete(Object message) {
        if (!this.messageList.isEmpty()) {
            return message.equals(getLastValueFromList() instanceof AckMessage);
        }
        return false;
    }

    public void addMessageToList(IMessage message) {
        this.messageList.put(message.getTimestamp(), message);
    }

    public boolean clearMessageList() {
        this.messageList.clear();
        return true;
    }

    public boolean getSessionComplete() {
        return this.sessionComplete;
    }

    public void setSessionComplete(boolean sessionComplete) {
        this.sessionComplete = sessionComplete;
    }
}

