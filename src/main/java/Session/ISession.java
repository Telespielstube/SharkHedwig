package Session;

import Message.IMessage;

import java.util.Optional;

/**
 * Interface for all protcol sessions.
 */
public interface ISession {
    /**
     * if protocol is in transferor state.
     *
     * @param message    Message object to be processed by transferor
     *
     * @return
     */
    Optional<Object> transferor(IMessage message, String sender);

    /**
     * If protocol is in transferee state.
     *
     * @param message
     *
     * @return
     */
    Optional<Object> transferee(IMessage message, String sender);

    /**
     * Returns the current state of the session.
     *
     * @return  boolean value of the session state.
     */
    boolean getSessionComplete();

    /**
     * If all messages of a session are exchanged the list needs to be checked if
     * all messages are cleared out.
     *
     * @param message    Message object.
     * @return           true if list is cleared.
     */
    void setSessionComplete(boolean complete);
}
