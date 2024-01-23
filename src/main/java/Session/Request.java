package Session;

import DeliveryContract.DeliveryContract;
import DeliveryContract.ShippingLabel;
import Message.Message;
import Message.IMessage;
import Message.MessageFlag;
import Message.Request.*;
import Misc.LogEntry;
import Misc.Logger;
import Misc.Utilities;
import Setup.AppConstant;

import java.util.Optional;
import java.util.TreeMap;

public class Request extends AbstractSession {

    private Offer offer;
    private OfferReply offerReply;
    private Confirm confirm;
    private DeliveryContract deliveryContract;
    private AckMessage ackMessage;
    private LogEntry logEntry;
    private Optional<Message> optionalMessage;

    public Request() {
        this.messageList = new TreeMap<>();
        this.optionalMessage = Optional.empty();
    }

    @Override
    public Optional<Object> transferor(IMessage message, String sender) {
        switch(message.getMessageFlag()) {
            case OFFER:
                handleOffer((Offer) message);
                break;
            case CONFIRM:
                handleConfirm((Confirm) message);
                break;
            case READY:
                handleAckMessage((AckMessage) message);
                if (this.optionalMessage.isPresent()) {
                    this.logEntry = new LogEntry(this.optionalMessage.get().getUUID(), Utilities.createReadableTimestamp(),
                            this.deliveryContract.getShippingLabel().getPackageDestination(),true, AppConstant.PEER_NAME.toString(), sender);
                    Logger.writeLog(logEntry.toString(), AppConstant.REQUEST_LOG_PATH.toString() +
                            this.optionalMessage.get().getUUID());
                }
                break;
            default:
                System.err.println("Missing message flag.");
                break;
        }
        if (!this.optionalMessage.isPresent()) {
            clearMessageList();
        } else {
            addMessageToList(this.optionalMessage.get());
        }
        return Optional.of(this.optionalMessage);
    }

    @Override
    public Optional<Object> transferee(IMessage message, String sender) {
        switch(message.getMessageFlag()) {
            case OFFER_REPLY:
                handleOfferReply((OfferReply) message);
                break;
            case CONFIRM:
                handleConfirm((Confirm) message);
                break;
            case ACK:
                handleAckMessage((AckMessage) message);
                if (this.optionalMessage.isPresent()) {
                    this.logEntry = new LogEntry(this.optionalMessage.get().getUUID(), Utilities.createReadableTimestamp(),
                            this.deliveryContract.getShippingLabel().getPackageDestination(), true, AppConstant.PEER_NAME.toString(), sender);
                    Logger.writeLog(logEntry.toString(), AppConstant.REQUEST_LOG_PATH.toString() +
                            this.optionalMessage.get().getUUID());
                }
                break;
            default:
                clearMessageList();
                break;
        }
        if (!this.optionalMessage.isPresent()) {
            clearMessageList();
        } else {
            addMessageToList(this.optionalMessage.get());
        }
        return Optional.of(this.optionalMessage);
    }

    /**
     * Processes the Offer data received from the Transferee.
     *
     * @param message    Offer message object.
     * @return           OfferReply object, or and enpty object if data were not verified.
     */
    private void handleOffer(Offer message) {
        ShippingLabel shippingData = this.deliveryContract.getShippingLabel();
        if (processOfferData(shippingData, message)) {
           this.optionalMessage = Optional.of(new OfferReply(Utilities.createUUID(), MessageFlag.OFFER_REPLY, Utilities.createTimestamp(), shippingData.getPackageWeight(), shippingData.getPackageDestination()));
        }
       // return Optional.empty();
    }


    /**
     * The recipient of the OfferRepy message has to process the received data sa well. Just to double
     * check all received data are verified with the local data set. This needs to be done!!!
     *
     * @param message    OfferReply message object
     * @return           Optional.empty if the calculation did not get verified, or Corfim message if data got verified.
     */
    private void handleOfferReply(OfferReply message) {
        if (compareTimestamp(message.getTimestamp(), timeOffset) && processOfferReplyData(message)) {
            this.optionalMessage = Optional.of(new Confirm(Utilities.createUUID(), MessageFlag.CONFIRM, Utilities.createTimestamp(), true));
        }
        //return Optional.empty();
    }

    /**
     * Validates the received Confirm message object and if no Confirm object is already saved
     * a new Confirm object gets created.
     *
     * @param message    Confirm messge object.
     * @return           An empty optional if a Confirm object is found.
     */
    private void handleConfirm(Confirm message) {
        Object object = getLastValueFromList();
        if (compareTimestamp(message.getTimestamp(), timeOffset) && message.getConfirmed() && (!(object instanceof Confirm))) {
            this.optionalMessage = Optional.of(new Confirm(Utilities.createUUID(), MessageFlag.CONFIRM, Utilities.createTimestamp(), true));
        }
       // return Optional.empty();
    }

    /**
     * The message sends an AckMessage signal the session is passed.
     *
     * @return    True if message check was valid. False if not
     */
    private void handleAckMessage(AckMessage ackMessage)  {
        if (compareTimestamp(ackMessage.getTimestamp(), timeOffset) && ackMessage.getIsAck()
                && !ackMessage.getMessageFlag().equals(MessageFlag.READY)) {
            this.optionalMessage =  Optional.of(new AckMessage(Utilities.createUUID(), MessageFlag.READY, Utilities.createTimestamp(), true));
        }
        //return Optional.empty();
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
        double freightWeight = message.getMaxFreightWeight();
        double flight = message.getFlightRange();
        // This need to be done when the battery and location component is implemented.
        return true;
    }
}
