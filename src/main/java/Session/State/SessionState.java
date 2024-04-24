package Session.State;

import DeliveryContract.DeliveryContract;
import DeliveryContract.ShippingLabel;
import Message.Messageable;
import ProtocolRole.ProtocolRole;
import java.util.Optional;

public interface SessionState {

    /**
     * Handles the incoming message based on the current protocol role.
     *
     * @param message             Received message object.
     * @param protocolRole        Represents the protocol role transferor or transferee.
     * @param shippingLabel       contains the ShippingLabel object reference.
     * @param deliveryContract    Contains the DeliveryContract object reference.
     * @param sender              The sender of the received message.
     *
     *
     * @return           An Optional if the message was processed correctly or and empty Optional container if not.
     */
    Optional<Message.Message> handle(Messageable message, ProtocolRole protocolRole, String sender);

    /**
     * The current state transitions to the following state.
     */
    void nextState();

    /**
     * the current state is reset to the initial NoSession state.
     */
    void resetState();
}
