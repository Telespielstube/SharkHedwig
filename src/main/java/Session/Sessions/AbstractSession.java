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

    public abstract Optional<Object> transferor(IMessage message, String sender);

    public abstract Optional<Object> transferee(IMessage message, String sender);

    public boolean compareTimestamp(long timestamp) {
        if (timestamp - this.messageList.lastKey() < this.timeOffset) {
            return true;
        }
        return false;
    }

    public Object getEntry(int index) {
        return  this.messageList.get(index);
    }
    public Object getLastValueFromList() {
        return this.messageList.get(this.messageList.lastKey());
    }

    @Override
    public boolean sessionComplete(Object message) {
        return message.equals(getLastValueFromList() instanceof AckMessage);
    }

    public void addMessageToList(IMessage message) {
        this.messageList.put(message.getTimestamp(), message);
    }

    public boolean clearMessageList() {
        this.messageList.clear();
        return true;
    }
}
