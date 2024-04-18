package ProtocolRole.State;

import DeliveryContract.DeliveryContract;
import DeliveryContract.ShippingLabel;
import Message.*;
import Message.Contract.*;
import Message.NoSession.Advertisement;
import Message.NoSession.Solicitation;
import Message.Request.Confirm;
import Message.Request.Offer;
import Message.Request.OfferReply;
import Misc.*;
import ProtocolRole.ProtocolRole;
import Message.MessageList;
import Session.Session;
import Setup.AppConstant;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.asap.crypto.ASAPCryptoAlgorithms;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * The Transferor subclass implements a behavior associated with a state of the ProtocolRole context class.
 */
public class Transferor implements ProtocolState {
    private final ProtocolRole protocolRole;
    private final Session session;
    private ShippingLabel shippingLabel;
    private MessageList messageList;
    private Optional<Message> optionalMessage;
    private int timeOffset = 5000;
    private String sender;

    public Transferor(ProtocolRole protocolRole, Session session) {
        this.protocolRole = protocolRole;
        this.session = session;
        this.optionalMessage = Optional.empty();
    }

    @Override
    public Optional<Message> handle(Messageable message, String sender) {
        this.sender = sender;
        switch (message.getMessageFlag()) {
            case ADVERTISEMENT:
                handleAdvertisement((Advertisement) message);
                this.session.getNoSessionState().nextState();
            case OFFER:
                handleOffer((Offer) message, shippingLabel);
                break;
            case CONFIRM:
                handleConfirm((Confirm) message);
                saveData();
                this.session.getRequestState().nextState();
                break;
            case AFFIRM:
                handleAffirm((Affirm) message);
                break;
            case READY_TO_PICK_UP:
                handleReadyToPickUp((Release) message);
                break;
            case COMPLETE:
                handleComplete((Complete) message);
                saveData();
                this.session.getContractState().nextState();
                break;
            default:
                System.err.println(Utilities.formattedTimestamp() + "Missing message flag.");
                this.session.getCurrentSessionState().resetState();
                break;
        }
        return this.optionalMessage;
    }

    private void handleReadyToPickUp(Release message) {
        if (this.messageList.compareTimestamp(message.getTimestamp(), timeOffset)) {
            this.optionalMessage = Optional.of(new Release(Utilities.createUUID(), MessageFlag.READY_TO_PICK_UP,
                    Utilities.createTimestamp()));
        }
    }

    @Override
    public void changeRole() {
        this.protocolRole.setProtocolState(this.protocolRole.getTranfereeState());
    }


    /**
     * Processes the received Adverntisement message and creates Solocitation message object.
     *
     * @param message    message object.
     */
    private void handleAdvertisement(Advertisement message) {
        if (message.getAdTag()) {
            this.optionalMessage = Optional.of(new Solicitation(Utilities.createUUID(), MessageFlag.SOLICITATION,
                    Utilities.createTimestamp(), true));
        }
    }

    /**
     * Processes the Offer data received from the Transferee.
     *
     * @param message Offer message object.
     * @return OfferReply object, or and enpty object if data were not verified.
     */
    private void handleOffer(Offer message, ShippingLabel shippingLabel) {
        if (MessageList.compareTimestamp(message.getTimestamp(), this.timeOffset)
                && verifyOfferData(message) && processOfferData(message)) {
            this.optionalMessage = Optional.of(new OfferReply(Utilities.createUUID(), MessageFlag.OFFER_REPLY,
                    Utilities.createTimestamp(), shippingLabel.get().getPackageWeight(),
                    shippingLabel.get().getPackageDestination()));
        }
    }


    private void handleConfirm(Confirm message) {
        if (MessageList.compareTimestamp(message.getTimestamp(), timeOffset)) {
            this.optionalMessage = checkContractState();
        }
    }

    /**
     * Approves received Affirm message object by checking the timestamp, validates the signed transferee field and
     * fills in the last remaining field "signatureTransferor".
     *
     * @param message    Affirm messge object.
     */
    private void handleAffirm(Affirm message) {
        if (MessageList.compareTimestamp(message.getTimestamp(), this.timeOffset)) {
            byte[] signedTransfereeField = message.getDeliveryContract().getTransitRecord().getLastElement().getSignatureTransferee() ;
            byte[] byteTransitEntry = MessageHandler.objectToByteArray(this.transitRecord.getLastElement());
            try {
                if (ASAPCryptoAlgorithms.verify(signedTransfereeField, byteTransitEntry, sender, this.sharkPKIComponent.getASAPKeyStore())) {
                    this.signedField = ASAPCryptoAlgorithms.sign(byteTransitEntry, this.sharkPKIComponent);
                    this.transitRecord.getLastElement().setSignatureTransferor(this.signedField);
                }
            } catch (ASAPSecurityException e) {
                System.err.println(Utilities.formattedTimestamp() + "Caught an ASAPSecurityException: " + e.getMessage());
                throw new RuntimeException(e);
            }
            this.optionalMessage = Optional.of(new PickUp(Utilities.createUUID(), MessageFlag.PICK_UP, Utilities.createTimestamp(), this.transitRecord));
        }
    }

    /**
     * The Complete message is sent by the transferee and marks the completion of the transaction.
     *
     * @param message    Complete message object.
     */
    private void handleComplete(Complete message) {
        timeOffset = 30000;
        if (this.messageList.compareTimestamp(message.getTimestamp(), this.timeOffset)) {
            // Send a message to the owners email address that a package is handed over to another drone.
            //notificationService.newMessage(DeliveryContract deliveryContract);

        } else {
            // Send a message to the owners email address that a package is lost
            //notificationService.newMessage(DeliveryContract deliveryContract);
        }
    }

    /**
     * Checks the state of the contract. This method is called from the request session. To initiate the contract session
     * and sending the correct contract.
     *
     * @return    Optional message ContractDocument containing the DeliveryContract.
     */
    public Optional<Message> checkContractState() {
        // Check object state to make sure to send the contract documents only once.
        if (!this.contractState) {
            createDeliveryContract(this.sender);
        } else if (this.deliveryContract.getTransitRecord().getListSize() > 1
                && !this.deliveryContract.getTransitRecord().getLastElement().getTransferor().equals(AppConstant.PEER_NAME)) {
            updateTransitRecord(this.sender);
        }
        return this.optionalMessage;
    }

    /**
     * Creates the contract document object. The ShippingLabel object is already in memory and the TransitRecord object
     * gets created now.
     *
     * @param receiver The sender of the message is the receiver of the newly created DeliveryContract object.
     */
    private void createDeliveryContract(String receiver) {
        this.deliveryContract = new DeliveryContract(receiver, geoSpatial);
        this.contractState = ContractState.CREATED.getState();
        this.optionalMessage = Optional.of(new ContractDocument(Utilities.createUUID(), MessageFlag.CONTRACT_DOCUMENT, Utilities.createTimestamp(), this.deliveryContract));
    }

    /**
     * Saves the important session data to the give path constant.
     */
    private void saveData() {
        if (this.optionalMessage.isPresent()) {
            LogEntry logEntry = new LogEntry(this.optionalMessage.get().getUUID(), Utilities.formattedTimestamp(),
                    this.deliveryContract.getShippingLabel().getPackageDestination(), true, AppConstant.PEER_NAME.toString(), sender);
            Logger.writeLog(logEntry.toString(), AppConstant.REQUEST_LOG_PATH.toString() +
                    this.optionalMessage.get().getUUID());
        }
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
}
