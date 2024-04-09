package Session;

import DeliveryContract.ShippingLabel;
import Message.Messageable;
import Message.Message;

import java.util.Optional;

/**
 * Interface for all protcol sessions.
 */
public interface Session {
    /**
     * if protocol is in transferor state.
     *
     * @param message    Message object to be processed by transferor
     *
     * @return
     */
    Optional<Message> transferor(Messageable message, ShippingLabel shippingLabel, String sender);

    /**
     * If protocol is in transferee state.
     *
     * @param message
     *
     * @return
     */
    Optional<Message> transferee(Messageable message, String sender);

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
