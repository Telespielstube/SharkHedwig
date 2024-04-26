package ProtocolRole.State;

import DeliveryContract.DeliveryContract;
import DeliveryContract.ShippingLabel;
import Location.GeoSpatial;
import Message.Message;
import Message.Messageable;
import Session.SessionManager;

import java.util.Optional;

/**
 * The interface for the protocol state machine. The defined methods are implemented in the sub-classes Transferee and
 * Transferor.
 */
public interface ProtocolState {

    /**
     * Handles the incoming message based on the current protocol role.
     *
     * @param message    Received message object.
     * @param sender     The sender of the received message.
     *
     * @return           An Optional if the message was processed correctly or and empty Optional container if not.
     */
    Optional<Message> handle(Messageable message, ShippingLabel shippingLabel, DeliveryContract deliveryContract, GeoSpatial geoSpatial, String sender);


}