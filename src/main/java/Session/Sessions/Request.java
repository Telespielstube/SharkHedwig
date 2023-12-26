package Session.Sessions;

import DeliveryContract.DeliveryContract;
import DeliveryContract.ShippingLabel;
import Message.IMessage;
import Message.MessageFlag;
import Message.Request.*;
import Battery.Battery;
import Location.GeoSpatial;
import Misc.LogEntry;
import Misc.SessionLogger;
import Misc.Utilities;
import Setup.Constant;

import java.util.Collections;
import java.util.Optional;
import java.util.TreeMap;

public class Request extends AbstractSession {

    private Offer offer;
    private OfferReply offerReply;
    private Confirm confirm;
    private Battery battery;
    private GeoSpatial geoCalculation;
    private DeliveryContract deliveryContract;
    private AckMessage ackMessage;

    public Request() {
        this.battery = new Battery();
        this.geoCalculation = new GeoSpatial();
        this.messageList = Collections.synchronizedSortedMap(new TreeMap<>());
    }

    @Override
    public Optional<Object> transferor(IMessage message, String sender) {
        Optional<AbstractRequest> messageObject = null;
        LogEntry logEntry;
        switch(message.getMessageFlag()) {
            case Offer:
                messageObject = Optional.of(handleOffer((Offer) message).get());
                break;
            case Confirm:
                messageObject = Optional.ofNullable(handleConfirm((Confirm) message).orElse(null));
                logEntry = new LogEntry(messageObject.get().getUuid(), messageObject.get().getTimestamp(), false, Constant.PeerName.getAppConstant(), sender);
                SessionLogger.writeEntry(logEntry.toString(), Constant.RequestLogPath.getAppConstant());
            case Ready:
                //If the isAck flag is set to 'true' in the Ready message. The transferor sets the concluded flag from 'false' to 'true'.
                messageObject = Optional.ofNullable(handleAckMessage((AckMessage) message).orElse(null));
                logEntry = new LogEntry(messageObject.get().getUuid(), messageObject.get().getTimestamp(), true, Constant.PeerName.getAppConstant(), sender);
                SessionLogger.writeEntry(logEntry.toString(), Constant.RequestLogPath.getAppConstant());
                break;
            default:
                System.err.println("Missing message flag.");
                break;
        }
        if (!messageObject.isPresent()) {
            clearMessageList();
        } else {
            addMessageToList(messageObject.get());

            SessionLogger.writeEntry(null, Constant.RequestLogPath.getAppConstant());
        }
        return Optional.ofNullable(messageObject);
    }

    @Override
    public Optional<Object> transferee(IMessage message, String sender) {
        Optional<AbstractRequest> messageObject = null;

        switch(message.getMessageFlag()) {
            case OfferReply:
                messageObject = Optional.ofNullable(handleOfferReply((OfferReply) message).orElse(null));
                break;
            case Confirm:
                messageObject = Optional.ofNullable(handleConfirm((Confirm) message).orElse(null));
                break;
            case Ack:
                messageObject = Optional.ofNullable(handleAckMessage((AckMessage) message).orElse(null));
                LogEntry logEntry = new LogEntry(messageObject.get().getUuid(), messageObject.get().getTimestamp(), true, Constant.PeerName.getAppConstant(), sender);
                SessionLogger.writeEntry(logEntry.toString(), Constant.RequestLogPath.getAppConstant());
                break;
            default:
                clearMessageList();
                break;
        }
        if (!messageObject.isPresent()) {
            clearMessageList();
        } else {
            addMessageToList(messageObject.get());
        }
        return Optional.ofNullable(messageObject);
    }

    /**
     * Processes the data
     * @param message
     * @return
     */
    private Optional<OfferReply> handleOffer(Offer message) {
       ShippingLabel shippingData = this.deliveryContract.getShippingLabel();
       if (processOfferData(shippingData, message)) {
           return Optional.of(new OfferReply(Utilities.createUUID(), MessageFlag.OfferReply, Utilities.createTimestamp(), shippingData.getPackageWeight(), shippingData.getPackageDestination()));
       }
       return Optional.empty();
    }


    /**
     * The recipient of the OfferRepy message has to process the received data sa well. Just to double
     * check all received data are verified with the local data set. This needs to be done!!!
     *
     * @param message    OfferReply message object
     * @return           Optional.empty if the calculation did not get verified, or Corfim message if data got verified.
     */
    private Optional<Confirm> handleOfferReply(OfferReply message) {

        if (compareTimestamp(message.getTimestamp())) {
            return Optional.of(new Confirm(Utilities.createUUID(), MessageFlag.Confirm, Utilities.createTimestamp(), true));
        }
        return Optional.empty();
    }

    /**
     * Validates the received Confirm message object and if no Confirm object is already saved
     * a new Confirm object gets created.
     *
     * @param message    Confirm messge object.
     * @return           An empty optional if a Confirm object is found.
     */
    private Optional<Confirm> handleConfirm(Confirm message) {
        Object object = getLastValueFromList();
        if (compareTimestamp(message.getTimestamp()) && message.getConfirmed() && (!(object instanceof Confirm))) {
            return Optional.of(new Confirm(Utilities.createUUID(), MessageFlag.Confirm, Utilities.createTimestamp(), true));
        }
        return Optional.empty();
    }

    /**
     * The message sends an AckMessage signal the session is passed.
     *
     * @return    True if message check was valid. False if not
     */
    public Optional<AckMessage> handleAckMessage(AckMessage ackMessage)  {
        boolean isFirstAck = compareTimestamp(ackMessage.getTimestamp()) && ackMessage.getIsAck() && ackMessage.getMessageFlag().equals(MessageFlag.Ack);
        if (isFirstAck) {
            return Optional.of(new AckMessage(Utilities.createUUID(), MessageFlag.Ready, Utilities.createTimestamp(), true));
        }
        return Optional.empty();
    }

    /**
     * This method processes the received offer data and calculates from the crucial data set if the
     * delivery service is possible. This need to be done!!!
     *
     * @return    True if all data is valid and package can be delivered to destination.
     */
    private boolean processOfferData(ShippingLabel shippingLabel, Offer message) {
        return true;
    }
}
