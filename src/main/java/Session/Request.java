package Session;

import DeliveryContract.DeliveryContract;
import DeliveryContract.ShippingLabel;
import Message.Message;
import Message.Messageable;
import Message.MessageFlag;
import Message.Request.*;
import Message.NoSession.Solicitation;
import Misc.LogEntry;
import Misc.Logger;
import Misc.Utilities;
import Setup.AppConstant;
import Location.Location;
import Battery.Battery;
import Location.Locationable;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class Request extends AbstractSession {

    private Contract contract;
    private String sender;
    private Solicitation solicitation;
    private Offer offer;
    private OfferReply offerReply;
    private Affirm confirm;
    private DeliveryContract deliveryContract;
    private Ack ack;
    private Battery battery;
    private Locationable geoSpatial;
    private MessageList messageList;
    private Optional<Message> optionalMessage;

    public Request(Contract contract, Battery battery, Locationable geoSpatial) {
        this.optionalMessage = Optional.empty();
        this.contract = contract;
        this.battery = battery;
        this.geoSpatial = geoSpatial;
    }

    @Override
    public Optional<Message> transferor(Messageable message, ShippingLabel shippingLabel, String sender) {
        this.sender = sender;
        switch(message.getMessageFlag()) {
            case OFFER:
                handleOffer((Offer) message, shippingLabel);
                break;
            case CONFIRM:
                handleConfirm((Affirm) message);
                break;
            case READY:
                handleAck((Ack) message);
                saveData();
                break;
            default:
                System.err.println(Utilities.formattedTimestamp() + "Missing message flag.");
                this.messageList.clearMessageList();
                break;
        }
        if (!this.optionalMessage.isPresent()) {
            this.messageList.clearMessageList();
        } else {
            this.messageList.addMessageToList(this.optionalMessage.get());
        }
        return this.optionalMessage;
    }

    @Override
    public Optional<Message> transferee(Messageable message, String sender) {
        switch(message.getMessageFlag()) {
            case SOLICITATION:
                handleSolicitation((Solicitation) message);
                break;
            case OFFER_REPLY:
                handleOfferReply((OfferReply) message);
                break;
            case ACK:
                handleAck((Ack) message);
                saveData();
                break;
            default:
                System.err.println(Utilities.formattedTimestamp() + "Missing message flag.");
                this.messageList.clearMessageList();
                break;
        }
        if (!this.optionalMessage.isPresent()) {
            this.messageList.clearMessageList();
        } else {
            this.messageList.addMessageToList(this.optionalMessage.get());
        }
        return this.optionalMessage;
    }

    /**
     * Processes the Offer data received from the Transferee.
     *
     * @param message    Offer message object.
     * @return           OfferReply object, or and enpty object if data were not verified.
     */
    private void handleOffer(Offer message, ShippingLabel shippingLabel) {
        if (this.messageList.compareTimestamp(message.getTimestamp(), this.timeOffset) && verifyOfferData(message)) {
            if (processOfferData(message)) {
                this.optionalMessage = Optional.of(new OfferReply(Utilities.createUUID(), MessageFlag.OFFER_REPLY,
                    Utilities.createTimestamp(), shippingLabel.get().getPackageWeight(), shippingLabel.get().getPackageDestination()));
            }
        }
    }

    /**
     * The recipient of the OfferRepy message has to process the received data sa well. Just to double
     * check all received data are verified with the local data set. This needs to be done!!!
     *
     * @param message    OfferReply message object
     * @return           Optional.empty if the calculation did not get verified, or Confirm message if data got verified.
     */
    private void handleOfferReply(OfferReply message) {
        if (this.messageList.compareTimestamp(message.getTimestamp(), this.timeOffset) && processOfferReplyData(message)) {
            this.optionalMessage = Optional.of(new Affirm(Utilities.createUUID(), MessageFlag.CONFIRM, Utilities.createTimestamp(), true));
        }
    }

    /**
     * Validates the received Confirm message object.
     *
     * @param message    Confirm messge object.
     * @return           An empty optional if a Confirm object is found.
     */
    private void handleConfirm(Affirm message) {
        if (this.messageList.compareTimestamp(message.getTimestamp(), this.timeOffset) && message.getConfirmed()) {
            this.optionalMessage = Optional.of(new Ack(Utilities.createUUID(), MessageFlag.ACK, Utilities.createTimestamp(), true));
        }
    }

    /**
     * An Acknowledgment massage to signal that the second secure code was decrypted successfully. It compares the timestamp
     * checks the flag and checks if the last TreeMap entry equals Ack.
     *
     * @param message    The received AckMessage object.
     * @return           An Optional AckMessage object if timestamp and ack flag are ok
     *                   or an empty Optional if it's not.
     */
    private void handleAck(Ack message) {
        if (this.messageList.compareTimestamp(message.getTimestamp(), timeOffset) && message.getIsAck()) {
            if (message.getMessageFlag().equals(MessageFlag.ACK)) {
                this.optionalMessage = Optional.of(new Ack(Utilities.createUUID(), MessageFlag.READY,
                        Utilities.createTimestamp(), true));
                this.sessionComplete = true;
            } else if (!message.getMessageFlag().equals(MessageFlag.READY)) {
                this.optionalMessage = this.contract.checkContractState(this.sender);
                this.sessionComplete = true;
            }
        }
    }

    /**
     * The incoming solicitation message gives the signal to the transferee to create and send out the offer message to the
     * transeror drone.
     *
     * @param message    The received Solicitation message.
     */
    private void handleSolicitation(Solicitation message) {
        if (this.messageList.compareTimestamp(message.getTimestamp(), this.timeOffset) && message.getSolicitate()) {
            this.optionalMessage = Optional.of(new Offer(Utilities.createUUID(), MessageFlag.OFFER, Utilities.createTimestamp(),
                    this.battery.getCurrentBatteryLevel(), AppConstant.MAX_FREIGHT_WEIGHT.getInt(), this.geoSpatial.getCurrentLocation()));
        }
    }

    /**
     * Processes the received OfferReply attributes. This serves as a double check of the necessary delivery data.
     *
     * @param message    OfferReply message object
     * @return           True if the transferee is able to deliver, false if not.
     */
    private boolean processOfferReplyData(OfferReply message) {
        double packageWeight = message.getPackageWeight();
        Location packageDestination = message.getPackageDestination();
        // Code to process the received data.
        return true;
    }

    /**
     * This method processes the received offer data and calculates the crucial data set if the
     * delivery service is possible. This need to be done!!!
     *
     * @return    True if all data is valid and package can be delivered to destination.
     */
    private boolean processOfferData(Offer message) {
        double freightWeight = message.getMaxFreightWeight();
        double flight = message.getFlightRange();
        // This need to be done when the battery and location component is implemented.
        return true;
    }

    /**
     * The Offer message has no message to compare the timestamp to, Therefore the message content gets verified.
     *
     * @param message    Message content.
     * @return           true if verified, false if not.
     */
    private boolean verifyOfferData(Offer message) {
        return Stream.of(message.getUUID(), message.getMessageFlag(), message.getTimestamp(),
                message.getFlightRange(), message.getMaxFreightWeight(),
                message.getCurrentLocation()).anyMatch(Objects::nonNull);
    }

    /**
     * Saves the important session data to the give path constant.
     */
    private void saveData() {
        if (this.optionalMessage.isPresent()) {
            LogEntry logEntry = new LogEntry(this.optionalMessage.get().getUUID(), Utilities.formattedTimestamp(), this.deliveryContract.getShippingLabel().getPackageDestination(), true, AppConstant.PEER_NAME.toString(), sender);
            Logger.writeLog(logEntry.toString(), AppConstant.REQUEST_LOG_PATH.toString() +
                    this.optionalMessage.get().getUUID());
        }
    }
}
