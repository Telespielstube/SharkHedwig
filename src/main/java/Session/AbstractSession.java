package Session;

import Message.Contract.Complete;
import Message.IMessage;
import Message.Identification.AckMessage;

import java.util.Optional;
import java.util.SortedMap;

public abstract class AbstractSession implements ISession {

    protected SortedMap<Long, Object> messageList;
    protected boolean sessionComplete = false;
    protected int timeOffset = 5000;

    public abstract Optional<Object> transferor(IMessage message, String sender);

    public abstract Optional<Object> transferee(IMessage message, String sender);

    public boolean compareTimestamp(long timestamp, int timeOffset) {
        return timestamp - this.messageList.lastKey() < timeOffset;
    }

    public Object getLastValueFromList() {
        return this.messageList.get(this.messageList.lastKey());
    }

    public boolean getSessionComplete(Object message) {
        return this.sessionComplete;
    }

    public void addMessageToList(IMessage message) {
        this.messageList.put(message.getTimestamp(), message);
    }

    public void clearMessageList() {
        this.messageList.clear();
    }

    public boolean getSessionComplete() {
        return this.sessionComplete;
    }
}
