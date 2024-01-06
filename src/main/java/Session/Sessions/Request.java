package Session.Sessions;

import DeliveryContract.DeliveryContract;
import DeliveryContract.ShippingLabel;
import Message.IMessage;
import Message.MessageFlag;
import Message.Request.*;
import Session.LogEntry;
import Session.Logger;
import Misc.Utilities;
import Setup.Constant;

import java.util.Collections;
import java.util.Optional;
import java.util.TreeMap;

public class Request extends AbstractSession {

    private Offer offer;
    private OfferReply offerReply;
    private Confirm confirm;
    private DeliveryContract deliveryContract;
    private AckMessage ackMessage;

    public Request() {
        this.messageList = Collections.synchronizedSortedMap(new TreeMap<>());
    }

    @Override
    public Optional<Object> transferor(IMessage message, String sender) {
        Optional<AbstractRequest> messageObject = Optional.empty();
        LogEntry logEntry;
        switch(message.getMessageFlag()) {
            case Offer:
                messageObject = Optional.of(handleOffer((Offer) message).get());
                break;
            case Confirm:
                messageObject = Optional.ofNullable(handleConfirm((Confirm) message).orElse(null));
                break;
            case Ready:
                //If the isAck flag is set to 'true' in the Ready message. The transferor sets the concluded flag from 'false' to 'true' and saves the session
                messageObject = Optional.ofNullable(handleAckMessage((AckMessage) message).orElse(null));
                if (messageObject.isPresent()) {
                    logEntry = new LogEntry(messageObject.get().getUuid(), Utilities.createReadableTimestamp(), true, Constant.PeerName.getAppConstant(), sender);
                    Logger.writeEntry(logEntry.toString(), Constant.RequestLogPath.getAppConstant());
                }
                break;
            default:
                System.err.println("Missing message flag.");
                break;
        }
        if (!messageObject.isPresent()) {
            clearMessageList();
        } else {
            addMessageToList(messageObject.get());

            Logger.writeEntry(null, Constant.RequestLogPath.getAppConstant());
        }
        return Optional.of(messageObject);
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
                LogEntry logEntry = new LogEntry(messageObject.get().getUuid(), Utilities.createReadableTimestamp(),
                        true, Constant.PeerName.getAppConstant(), sender);
                Logger.writeEntry(logEntry.toString(), Constant.RequestLogPath.getAppConstant());
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
        return Optional.of(messageObject);
    }

    /**
     * Processes the Offer data received from the Transferee.
     *
     * @param message    Offer message object.
     * @return           OfferReply object, or and enpty object if data were not verified.
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
        if (compareTimestamp(message.getTimestamp(), timeOffset) && processOfferReplyData(message)) {
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
        if (compareTimestamp(message.getTimestamp(), timeOffset) && message.getConfirmed() && (!(object instanceof Confirm))) {
            return Optional.of(new Confirm(Utilities.createUUID(), MessageFlag.Confirm, Utilities.createTimestamp(), true));
        }
        return Optional.empty();
    }

    /**
     * The message sends an AckMessage signal the session is passed.
     *
     * @return    True if message check was valid. False if not
     */
    private Optional<AckMessage> handleAckMessage(AckMessage ackMessage)  {
        boolean isFirstAck = compareTimestamp(ackMessage.getTimestamp(), timeOffset) && ackMessage.getIsAck() && ackMessage.getMessageFlag().equals(MessageFlag.Ack);
        if (isFirstAck) {
            return Optional.of(new AckMessage(Utilities.createUUID(), MessageFlag.Ready, Utilities.createTimestamp(), true));
        }
        return Optional.empty();
    }

    /**
     * Processes the received OfferReply attributes. This serves as a double check of the necessary delivery data.
     *
     * @param message    OfferReply message object
     * @return           True if the transferee is able to deliver, false if not.
     */
    private boolean processOfferReplyData(OfferReply message) {
        return true;
    }

    /**
     * This method processes the received offer data and calculates the crucial data set if the
     * delivery service is possible. This need to be done!!!
     *
     * @return    True if all data is valid and package can be delivered to destination.
     */
    private boolean processOfferData(ShippingLabel label, Offer message) {

        // This need to be done when the battery and location component is implemented.
        return true;
    }
}
