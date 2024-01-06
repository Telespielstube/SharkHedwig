package Session.Sessions;

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
     * Compares to timestamps if the difference is less than the offset of 5 seconds.
     *
     * @param timestamp    timestamp of received message.
     * @return             True if difference is less than offset. False if it is greater than.
     */
    boolean compareTimestamp(long timestamp, int timeOffset);

    /**
     * Gets the last value from the message TreeMap, which is a (K,V) map.
     *
     * @return               Last object from TreeMap.
     */
    Object getLastValueFromList();

    /**
     * If all messages of a session are exchanged the list needs to be checked if
     * all messages are cleared out.
     *
     * @param message    Message object.
     * @return           true if list is cleared.
     */
    boolean setSessionComplete(Object message);

    /**
     * Adds a message object to the TreeMap.
     */
    void addMessageToList(IMessage message);

    /**
     * Gets the session complete attrubute.
     */
    boolean getSessionComplete();

    /**
     * Clears the messageList TreeMap object.
     *
     * @return    True if list is clear,
     */
    boolean clearMessageList();
}
