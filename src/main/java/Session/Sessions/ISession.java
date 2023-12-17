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
    <T> T unpackMessage(T message);

    /**
     * Checks the TreeMap object if all neccessaray Message objects where sent and received.
     *
     * @return    Treu if the size of the TreeMap matches the expected message number of session messages.
     */
    boolean isSessionComplete();
}
