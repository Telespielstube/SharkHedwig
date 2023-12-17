package Session.Sessions;

import Message.Identification.Challenge;

/**
 * Interface for all protcol sessions.
 */
public interface ISession {
    /**
     * Unpacks the generic Message object to the according Message for this session it can be Challenge,
     * Response or an Ack message.
     */
    Object unpackMessage(Object message);

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
