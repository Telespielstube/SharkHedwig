package ProtocolRole.State;

import Battery.Battery;
import DeliveryContract.DeliveryContract;
import DeliveryContract.ShippingLabel;
import DeliveryContract.TransitRecord;
import DeliveryContract.ContractState;
import Location.GeoSpatial;
import Message.Contract.*;
import Message.Message;
import Message.Request.Confirm;
import Message.MessageCache;
import Message.MessageFlag;
import Message.MessageHandler;
import Message.Messageable;
import Message.Request.Offer;
import Message.Request.OfferReply;
import Message.NoSession.Solicitation;
import Misc.LogEntry;
import Misc.Logger;
import Misc.Utilities;
import ProtocolRole.ProtocolRole;
import Setup.AppConstant;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.asap.crypto.ASAPCryptoAlgorithms;
import net.sharksystem.pki.SharkPKIComponent;

import java.util.Optional;

/**
 * The Transferee subclass implements a behavior associated with a state of the ProtocolRole context class.
 */
public class Transferee implements ProtocolState {
    private final ProtocolRole protocolRole;
    private Battery battery;
    private GeoSpatial geoSpatial;
    private String sender;
    private ShippingLabel shippingLabel;
    private DeliveryContract deliveryContract;
    private SharkPKIComponent sharkPKIComponent;
    private Optional<Message> optionalMessage;
    private TransitRecord transitRecord;
    private boolean contractState;
    private int timeOffset;

    public Transferee(ProtocolRole protocolRole, Battery battery, GeoSpatial geoSpatial,
                      SharkPKIComponent sharkPKIComponent) {
        this.protocolRole = protocolRole;

        this.battery = battery;
        this.geoSpatial = geoSpatial;
        this.sharkPKIComponent = sharkPKIComponent;
        this.timeOffset = 5000;
    }

    @Override
    public Optional<Message> handle(Messageable message, ShippingLabel shippingLabel, DeliveryContract deliveryContract,
                                    GeoSpatial geoSpatial, String sender) {
        this.shippingLabel = shippingLabel;
        this.deliveryContract = deliveryContract;
        this.geoSpatial = geoSpatial;
        this.sender = sender;
        this.optionalMessage = Optional.empty();

        switch(message.getMessageFlag()) {
            case SOLICITATION:
                handleSolicitation((Solicitation) message);
                break;
            case OFFER_REPLY:
                handleOfferReply((OfferReply) message);
                saveData(AppConstant.REQUEST_LOG_PATH, message);
                break;
            case CONTRACT_DOCUMENT:
                handleContract((ContractDocument) message);
                break;
            case PICK_UP:
                handlePickUp((PickUp) message, sender);
                saveData(AppConstant.DELIVERY_CONTRACT_LOG_PATH, message);
                break;
            case RELEASE:
                handleRelease((Release) message);
                this.protocolRole.changeRole();
                break;
            default:
                System.err.println(Utilities.formattedTimestamp() + "Message flag was incorrect: " + message.getMessageFlag());
                break;
        }
        if (this.optionalMessage.isPresent() && MessageCache.getMessageCacheSize() <= MessageCache.getTranfereeCacheSize() ) {
            MessageCache.addMessage(optionalMessage.get());
        } else {
            MessageCache.clearMessageList();
        }
        return this.optionalMessage;
    }

    /**
     * The transferee needs to store the DeliveryContract in memory too.
     *
     * @param message    The DeliveryContract object reference.
     */
    private void inMemoDeliveryContract(DeliveryContract message) {
        this.deliveryContract = (DeliveryContract) message.clone();
    }

    /**
     * The incoming solicitation message gives the signal to the transferee to create and send out the offer message to the
     * transeror drone.
     *
     * @param message    The received Solicitation message.
     */
    private void handleSolicitation(Solicitation message) {
        if (MessageCache.compareTimestamp(message.getTimestamp(), this.timeOffset)) {
            this.optionalMessage = Optional.of(new Offer(Utilities.createUUID(), MessageFlag.OFFER, Utilities.createTimestamp(),
                    100, AppConstant.MAX_FREIGHT_WEIGHT.getInt(), null));
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
        if (MessageCache.compareTimestamp(message.getTimestamp(), this.timeOffset))
            if (processOfferReplyData(message)) {
                this.optionalMessage = Optional.of(new Confirm(Utilities.createUUID(), MessageFlag.CONFIRM, Utilities.createTimestamp()));
        }
    }

    /**
     * Processes the received OfferReply attributes. This serves as a double check of the necessary delivery data.
     *
     * @param message    OfferReply message object
     * @return           True if the transferee is able to deliver, false if not.
     */
    private boolean processOfferReplyData(OfferReply message) {
        //double packageWeight = message.getPackageWeight();
        //Location packageDestination = message.getPackageDestination();
        // Code to process the received data.
        return true;
    }

    /**
     * Handles all things data processing after receiving contract documents.
     *
     * @param message    PickUp message object.
     */
    private void handleContract(ContractDocument message) {
        if (message.getDeliveryContract() != null) {
            inMemoDeliveryContract(message.getDeliveryContract());
            this.transitRecord = message.getDeliveryContract().getTransitRecord();
            this.geoSpatial.setPickUpLocation(this.transitRecord.getLastElement().getPickUpLocation());
            try {
                byte[] signedField = ASAPCryptoAlgorithms.sign(MessageHandler.objectToByteArray(
                        this.transitRecord.getLastElement()), sharkPKIComponent);
                this.transitRecord.getLastElement().setSignatureTransferee(signedField);
            } catch (ASAPSecurityException e) {
                System.err.println(Utilities.formattedTimestamp() + "Caught an ASAPSecurityException: " + e.getMessage());
                throw new RuntimeException(e);
            }
            this.optionalMessage = Optional.of(new Affirm(Utilities.createUUID(), MessageFlag.AFFIRM,
                    Utilities.createTimestamp(), this.deliveryContract));
        }
    }

    /**
     * The PickUp message is sent when the package is ready to pickup.
     *
     * @param message    PickUp message object.
     */
    private void handlePickUp(PickUp message, String sender) {
        byte[] signedTransferorField = message.getTransitRecord().getLastElement().getSignatureTransferor();
        byte[] byteTransitEntry = MessageHandler.objectToByteArray(this.transitRecord.getLastElement());
        // Transferee needs to verify the transferor signature as well!!
        if (MessageCache.compareTimestamp(message.getTimestamp(), timeOffset)) {
            try {
                if (ASAPCryptoAlgorithms.verify(signedTransferorField, byteTransitEntry, sender, sharkPKIComponent)) {
                    this.transitRecord = message.getTransitRecord();
                }
            } catch (ASAPSecurityException e) {
                throw new RuntimeException(e);
            }
            this.optionalMessage = Optional.of(new Ready(Utilities.createUUID(), MessageFlag.READY_TO_PICK_UP,
                    Utilities.createTimestamp()));
        }
    }

    /**
     * An Acknowledgment massage to signal that the second secure code was decrypted successfully. It compares the timestamp
     * checks the flag and checks if the last TreeMap entry equals Ack.
     *
     * @param message    The received AckMessage object.
     */
    private void handleRelease(Release message)  {
        if (MessageCache.compareTimestamp(message.getTimestamp(), timeOffset)) {
            this.optionalMessage = Optional.of(new Complete(Utilities.createUUID(), MessageFlag.COMPLETE,
                    Utilities.createTimestamp()));
        }
    }

    /**
     * Saves the important session data to the give path constant.
     */
    private void saveData(AppConstant logPath, Messageable message) {
        String path = logPath.toString();
        String file = this.sender + Utilities.formattedTimestamp() + ".txt";
        if (this.optionalMessage.isPresent()) {
            if (message instanceof OfferReply) {
                Logger.writeLog(new LogEntry(this.sender, AppConstant.PEER_NAME.toString(), Utilities.formattedTimestamp(), ((OfferReply) message).getPackageWeight(), ((OfferReply) message).getPackageDestination(), true).getRequestLogEntry(), path + "/" + file);
            } else {
                Logger.writeLog(new LogEntry(this.deliveryContract).getDeliveryContractLogEntry(), file);
            }
        }
    }
}
