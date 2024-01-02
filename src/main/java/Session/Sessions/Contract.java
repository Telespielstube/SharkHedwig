package Session.Sessions;

import DeliveryContract.*;
import HedwigUI.UserInputBuilder;
import Location.Location;
import Location.IGeoSpatial;
import Message.Contract.*;
import Message.IMessage;
import Message.IMessageHandler;
import Message.MessageFlag;
import Misc.LogEntry;
import Misc.SessionLogger;
import Misc.Utilities;
import Setup.Constant;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.asap.crypto.ASAPCryptoAlgorithms;
import net.sharksystem.pki.SharkPKIComponent;

import java.util.*;

public class Contract extends AbstractSession {

    private SharkPKIComponent sharkPKIComponent;
    private IMessageHandler messageHandler;
    private ContractDocument contractDocument;
    private DeliveryContract deliveryContract;
    private IContractComponent shippingLabel;
    private IContractComponent transitRecord;
    private UserInputBuilder userInputBuilder;
    private IGeoSpatial geoSpatial;
    private Location location;
    private Confirm confirm;
    private PickUp pickUp;
    private Location pickupLocation;
    private AckMessage ackMessage;

    public Contract(IMessageHandler messageHandler, SharkPKIComponent sharkPKIComponent) {
        this.messageHandler = messageHandler;
        this.sharkPKIComponent = sharkPKIComponent;
        this.messageList = Collections.synchronizedSortedMap(new TreeMap<>());
    }

    @Override
    public Optional<Object> transferor(IMessage message, String sender) {
        Optional<AbstractContract> messageObject = Optional.empty();
        // Check object state to make sure to send the contract documents only once.
        if (!this.deliveryContract.getContractSent()) {
            messageObject = Optional.of(createDeliveryContract());
            this.deliveryContract.setContractSent(true);
        }
        switch(message.getMessageFlag()) {
            case Confirm:
                messageObject = Optional.ofNullable(handleConfirm((Confirm) message, sender).orElse(null));
                break;
            case Ack:
                messageObject = Optional.ofNullable(handleAckMessage((AckMessage) message).orElse(null));
                LogEntry logEntry = new LogEntry(messageObject.get().getUuid(), messageObject.get().getTimestamp(),
                                    new Location(pickupLocation.getLatitude(), pickupLocation.getLongitude()),
                          true, Constant.PeerName.getAppConstant(), sender);
                SessionLogger.writeEntry(logEntry.toString(), Constant.RequestLogPath.getAppConstant());
                break;
            default:
                System.err.println("Message flag was incorrect: " + message.getMessageFlag());
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

    @Override
    public Optional<Object> transferee(IMessage message, String sender) {
        Optional<AbstractContract> messageObject = Optional.empty();
        switch(message.getMessageFlag()) {
            case ContractDocument:
                messageObject = Optional.ofNullable(handleContract((ContractDocument) message).orElse(null));
                break;
            case PickUp:
                messageObject = Optional.ofNullable(handlePickUp((PickUp) message).orElse(null));
                break;
            case Ack:
                messageObject = Optional.ofNullable(handleAckMessage((AckMessage) message).orElse(null));
                LogEntry logEntry = new LogEntry(messageObject.get().getUuid(), messageObject.get().getTimestamp(),
                                    new Location(pickupLocation.getLatitude(), pickupLocation.getLongitude()),
                          true, Constant.PeerName.getAppConstant(), sender);
                SessionLogger.writeEntry(logEntry.toString(), Constant.RequestLogPath.getAppConstant());
                break;
            default:
                System.err.println("Message flag was incorrect: " + message.getMessageFlag());
                clearMessageList();
                break;
        }
        if (!messageObject.isPresent()) {
            clearMessageList();
        } else {
            addMessageToList(messageObject.get());
        }
        return Optional.empty();
    }

    /**
     * Creates the contract document object. The ShippingLabel object is already in memory and the TransitRecord object
     * gets created now.
     *
     * @return    new ContractDocument message object.
     */
    public ContractDocument createDeliveryContract() {
        ShippingLabel label = this.deliveryContract.getShippingLabel();
        TransitRecord record = new TransitRecord();
        record.addEntry(new TransitEntry(0, label.getUUID(), Constant.PeerName.getAppConstant(),
                "", geoSpatial.getCurrentLocation(), Utilities.createTimestamp(), null));
        this.deliveryContract.setContractSent(true);
        return new ContractDocument(Utilities.createUUID(), MessageFlag.ContractDocument,
                Utilities.createTimestamp(), new DeliveryContract(label, record));
    }

    /**
     * Validates the received Confirm message object and if no Confirm object is already saved
     * a new Confirm object gets created.
     *
     * @param message    Confirm messge object.
     * @return           An empty optional if a Confirm object is found.
     */
    private Optional<PickUp> handleConfirm(Confirm message, String sender) {
        byte[] signedTransitEntry = new byte[0];
        if (compareTimestamp(message.getTimestamp()) && message.getConfirmed()) {
            String transferee = message.getDeliveryContract().getTransitRecord().getLastElement().getTransferee();
            if (transferee.equals(sender)) {
                signedTransitEntry = signTransitEntry(message);
            }
            return Optional.of(new PickUp(Utilities.createUUID(), MessageFlag.PickUp, Utilities.createTimestamp(), signedTransitEntry));
        }
        return Optional.empty();
    }

    /**
     * Handles all things data processing after receiving contract documents.
     *
     * @param message    PickUp message object.
     * @return           An optional if the message passed the timestamp and flag checks or empty if not.
     */
    private Optional<Confirm> handleContract(ContractDocument message) {
        if (message.getDeliveryContract() != null) {
            this.deliveryContract = storeDeliveryContract(message.getDeliveryContract());
            TransitEntry lastEntry = this.deliveryContract.getTransitRecord().getLastElement();
            lastEntry.setTransferee(Constant.PeerName.getAppConstant());
            this.geoSpatial.setPickUpLocation(lastEntry.getHandoverLocation());
            return Optional.of(new Confirm(Utilities.createUUID(), MessageFlag.Confirm, Utilities.createTimestamp(), this.deliveryContract, true));
        }
        return Optional.empty();
    }

    /**
     * The transferee needs to save the received DeliveryContract object locally. So it can be handed over to the next Hedwig device
     * at a later time.
     *
     * @param message    The DeliveryContract object.
     *
     * @return           Newly created DeliveryContract object.
     */
    private DeliveryContract storeDeliveryContract(DeliveryContract message) {
        ShippingLabel label = message.getShippingLabel();
        this.shippingLabel = new ShippingLabel(label.getUUID(), label.getSender(), label.getOrigin(), label.getPackageOrigin(),
                label.getRecipient(), label.getDestination(), label.getPackageDestination(), label.getPackageWeight());
        List<TransitEntry> entries = message.getTransitRecord().getAllEntries();
        return new DeliveryContract(this.shippingLabel, new TransitRecord(entries));
    }

    /**
     * Digitally signs the last TransitEntry object.
     *
     * @param message    Confirm message holds the DeliveryContract + TransitRecor object.
     */
    public byte[] signTransitEntry(Confirm message) {
        byte[] unsignedEntry = messageHandler.objectToByteArray(message.getDeliveryContract().getTransitRecord().getLastElement());
        byte[] signedEntry;
        try {
            signedEntry = ASAPCryptoAlgorithms.sign(unsignedEntry, this.sharkPKIComponent.getASAPKeyStore());
        } catch (ASAPSecurityException e) {
            throw new RuntimeException(e);
        }
        return signedEntry;
    }

    /**
     * This message holds the Location object of where the pick up/hand over of the package is about to happen
     *
     * @param message    PickUp message object.
     * @return           An optional if the message passed the timestamp and flag checks or empty if not.
     */
    private Optional<AckMessage> handlePickUp(PickUp message) {
        if (compareTimestamp(message.getTimestamp())) {
            this.deliveryContract.getTransitRecord().getLastElement().setDigitalSignature(message.getSignedTransitRecord());
            return Optional.of(new AckMessage(Utilities.createUUID(), MessageFlag.Ack, Utilities.createTimestamp(), true));
        }
        return Optional.empty();
    }

    /**
     * An Acknowledgment maesage to signal that the PickUpMessage was received.
     *
     * @param ackMessage    The received AckMessage object.
     * @return              An otional AckMessage object if timestamp and ack flag are ok
     *                      or an empty Optional if its not.
     */
    public Optional<AckMessage> handleAckMessage(AckMessage ackMessage)  {
        if (compareTimestamp(ackMessage.getTimestamp()) && ackMessage.getIsAck()) {
            return Optional.of(ackMessage);
        }
        return Optional.empty();
    }
}
