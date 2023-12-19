package Session.Sessions;

import Message.IMessage;
import Session.SessionState;

import java.util.Optional;
import java.util.SortedMap;

public abstract class AbstractSession implements ISession {

    protected SortedMap<Long, Object> messageList;
    private final long timeOffset = 5000;

    public abstract Optional<Object> transferor(IMessage message);

    public abstract Optional<Object> transferee(IMessage message);

    public boolean compareTimestamp(long timestamp) {
        boolean valid = false;
        if (this.messageList.lastKey() - timestamp < this.timeOffset) {
            valid = true;
        }
        return valid;
    }


//    /**
//     * If some message does not seem legit the complete communication sessions are reset to NoSession
//     * .
//     * @param sessionState    current session.
//     */
//    public void cancelSession(SessionState sessionState) {
//        sessionState.resetState();
//        this.messageList.clear();
//    }


    /**
     * If all messages of a session are exchanged the message list is cleared.
     *
     * @return    true if
     */
    public boolean isSessionComplete() {
        boolean isComplete = false;
        if (this.messageList.isEmpty()) {
            isComplete = true;
        }
        return isComplete;
    }
}

