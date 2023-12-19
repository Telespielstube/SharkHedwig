package Session.Sessions;

import Message.IMessage;

import java.util.Optional;

/**
 * Interface for all protcol sessions.
 */
public interface ISession {
    /**
     * if protocol is in transferor state.
     */
    Optional<Object> transferor(IMessage message);

    /**
     * If protocol is in transferee state.
     * @param message
     * @return
     */
    Optional<Object> transferee(IMessage message);

    /**
     * Compares to timestamps if the difference is less than the offset of 5 seconds.
     *
     * @param timestamp    timestamp of received message.
     * @return             True if difference is less than offset. False if it is greater than.
     */
    public boolean compareTimestamp(long timestamp);

    /**
     * Gets the last value from message TreeMap.
     *
     * @return               Last object from TreeMap.
     */
    public Object getLastValueFromList();

    /**
     * If all messages of a session are exchanged the list needs to be checked if
     * all messages are cleared out.
     *
     * @return    true if list is cleared.
     */
    boolean sessionComplete();
}
