package Session.Sessions;

import java.util.SortedMap;

public abstract class AbstractSession implements ISession {

    private SortedMap<Long, Object> messageList;
    private final long timeOffset = 5000;

    public abstract Object unpackMessage(Object message);

    public boolean compareTimestamp(long timestamp) {
        boolean valid = false;
        if (this.messageList.lastKey() - timestamp < this.timeOffset) {
            valid = true;
        }
        return valid;
    }

    public boolean isSessionComplete() {
        boolean isComplete = false;
        if (this.messageList.isEmpty()) {
            isComplete = true;
        }
        return isComplete;
    }
    }
}
