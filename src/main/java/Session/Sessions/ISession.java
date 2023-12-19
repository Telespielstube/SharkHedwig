package Session.Sessions;

import Message.IMessage;
import Message.Identification.Challenge;

import java.util.Optional;

/**
 * Interface for all protcol sessions.
 */
public interface ISession {
    /**
     * Unpacks the generic Message object to the according message based on their flag.
     */
    Optional<Object> transferor(IMessage message);

    Optional<Object> transferee(IMessage message);
    /**
     * Compares two timestamps. The passed timestamp relates to the received message and the tother timestamps
     * relates to the sent and saved message.
     *
     * @param timestamp    timestamp of received message.
     * @return             true if timestamp is valid the difference is not greater than 5 seconds.
     */
    public boolean compareTimestamp(long timestamp);
    /**
     * Checks the TreeMap object if all neccessaray Message objects where sent and received.
     *
     * @return    Treu if the size of the TreeMap matches the expected message number of session messages.
     */
    boolean isSessionComplete();
}
