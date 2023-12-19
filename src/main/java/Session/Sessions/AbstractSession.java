package Session.Sessions;

import Message.IMessage;
import Message.AckMessage;

import java.util.Optional;
import java.util.SortedMap;

public abstract class AbstractSession implements ISession {

    protected SortedMap<Long, Object> messageList;
    private final long timeOffset = 5000;

    public abstract Optional<Object> transferor(IMessage message);

    public abstract Optional<Object> transferee(IMessage message);

    public boolean compareTimestamp(long timestamp) {
        boolean valid = false;
        if (timestamp - this.messageList.lastKey() < this.timeOffset) {
            valid = true;
        }
        return valid;
    }

    public boolean handleAckMessage(AckMessage ackMessage) {
        boolean complete = false;
        if ( compareTimestamp(ackMessage.getTimestamp()) && ackMessage.getIsAck() ) {
            return complete = true;
        }
        return complete;
    }

    public boolean sessionComplete() {
        this.messageList.clear();
        return true;
    }
}

