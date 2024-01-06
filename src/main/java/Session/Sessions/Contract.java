package Session.Sessions;

import DeliveryContract.*;
import HedwigUI.UserInputObject;
import Location.Location;
import Location.IGeoSpatial;
import Message.Contract.*;
import Message.IMessage;
import Message.IMessageHandler;
import Message.MessageFlag;
import Message.MessageHandler;
import Session.LogEntry;
import Session.Logger;
import Misc.Utilities;
import Setup.Constant;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.asap.crypto.ASAPCryptoAlgorithms;
import net.sharksystem.pki.SharkPKIComponent;

import java.util.*;

public class Contract extends AbstractSession {

    private final SharkPKIComponent sharkPKIComponent;
    private final IMessageHandler messageHandler;
    private ContractDocument contractDocument;
    private DeliveryContract deliveryContract;
    private IDeliveryContract shippingLabel;
    private IDeliveryContract contractRecord;
    private UserInputObject userInputBuilder;
    private IGeoSpatial geoSpatial;
    private Location location;
    private Confirm confirm;
    private PickUp pickUp;
    private Location pickupLocation;
    private AckMessage ackMessage;
    private TransitRecord transitRecord;

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
            messageObject = Optional.of(createDeliveryContract(sender));
            this.deliveryContract.setContractSent(true);
        }
        switch(message.getMessageFlag()) {
            case Confirm:
                messageObject = Optional.ofNullable(handleConfirm((Confirm) message, sender).orElse(null));
                break;
            case Ack:
                messageObject = Optional.ofNullable(handleAckMessage((AckMessage) message).orElse(null));
                if (messageObject.isPresent()) {
                    LogEntry logEntry = new LogEntry(messageObject.get().getUuid(), Utilities.createReadableTimestamp(),
                            new Location(pickupLocation.getLatitude(), pickupLocation.getLongitude()), true,
                            Constant.PeerName.getAppConstant(), sender);
                    Logger.writeEntry(logEntry.toString(), Constant.RequestLogPath.getAppConstant());
                }
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
                messageObject = Optional.ofNullable(handlePickUp((PickUp) message, sender).orElse(null));

                break;
            case Ack:
                messageObject = Optional.ofNullable(handleAckMessage((AckMessage) message).orElse(null));
                if (messageObject.isPresent()) {
                    LogEntry logEntry = new LogEntry(messageObject.get().getUuid(), Utilities.createReadableTimestamp(),
                            geoSpatial.getCurrentLocation(), true,
                            Constant.PeerName.getAppConstant(), sender);
                    Logger.writeLog(logEntry.toString(), Constant.RequestLogPath.getAppConstant());
                }
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

    /**
     * Creates the contract document object. The ShippingLabel object is already in memory and the TransitRecord object
     * gets created now.
     *
     * @return    new ContractDocument message object.
     */
    public ContractDocument createDeliveryContract(String sender) {
        ShippingLabel label = this.deliveryContract.getShippingLabel();
        this.transitRecord = new TransitRecord();
        this.transitRecord.addEntry(new TransitEntry(this.transitRecord.countUp(), label.getUUID(), Constant.PeerName.getAppConstant(),
                sender, this.geoSpatial.getCurrentLocation(), Utilities.createTimestamp(), null, null));
        this.deliveryContract.setContractSent(true);
        return new ContractDocument(Utilities.createUUID(), MessageFlag.ContractDocument,
                Utilities.createTimestamp(), new DeliveryContract(label, this.transitRecord));
    }

    /**
     * Validates the received Confirm message object and verifies the signed transferee field.
     *
     * @param message    Confirm messge object.
     * @return           PickUp message, empty Optional if time or signature is not correct.
     */
    private Optional<PickUp> handleConfirm(Confirm message, String sender) {
        if (compareTimestamp(message.getTimestamp(), this.timeOffset) && message.getConfirmed()) {
            byte[] signedTransfereeField = message.getDeliveryContract().getTransitRecord().getLastElement().getSignatureTransferee();
            byte[] byteTransitEntry = MessageHandler.objectToByteArray(this.transitRecord.getLastElement());
            byte[] signedTransferor;
            try {
                if (ASAPCryptoAlgorithms.verify(signedTransfereeField, byteTransitEntry, sender, this.sharkPKIComponent.getASAPKeyStore())) {
                    signedTransferor = ASAPCryptoAlgorithms.sign(MessageHandler.objectToByteArray(this.transitRecord.getLastElement()), this.sharkPKIComponent);
                    this.transitRecord.getLastElement().setSignatureTransferor(signedTransferor);
                }
            } catch (ASAPSecurityException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            return Optional.of(new PickUp(Utilities.createUUID(), MessageFlag.PickUp, Utilities.createTimestamp(), this.transitRecord));
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
        byte[] signedTransitEntry;
        if (message.getDeliveryContract() != null) {
            this.transitRecord = message.getDeliveryContract().getTransitRecord();
            this.geoSpatial.setPickUpLocation(this.transitRecord.getLastElement().getPickUpLocation());
            try {
                signedTransitEntry = ASAPCryptoAlgorithms.sign(MessageHandler.objectToByteArray(this.transitRecord.getLastElement()), sharkPKIComponent);
                this.transitRecord.getLastElement().setSignatureTransferee(signedTransitEntry);
            } catch (ASAPSecurityException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            inMemoDeliveryContract(message.getDeliveryContract());
            return Optional.of(new Confirm(Utilities.createUUID(), MessageFlag.Confirm, Utilities.createTimestamp(), this.deliveryContract, true));
        }
        return Optional.empty();
    }

    /**
     * The transferee needs to store the DeliveryContract in memory until the transferor signed the trasnit record entry too.
     *
     * @param message    The DeliveryContract object.
     */
    private DeliveryContract inMemoDeliveryContract(DeliveryContract message) {
        ShippingLabel label = message.getShippingLabel();
        this.shippingLabel = new ShippingLabel(label.getUUID(), label.getSender(), label.getOrigin(), label.getPackageOrigin(),
                label.getRecipient(), label.getDestination(), label.getPackageDestination(), label.getPackageWeight());
        this.deliveryContract = new DeliveryContract(this.shippingLabel, new TransitRecord(this.transitRecord.getAllEntries()));
    }

    /**
     * The PickUp message is sent when the package is ready to pickup.
     *
     * @param message    PickUp message object.
     * @return           An optional if the message passed the timestamp and flag checks or empty if not.
     */
    private Optional<AckMessage> handlePickUp(PickUp message, String sender) {
        byte[] signedTransferorField = message.getTransitRecord().getLastElement().getSignatureTransferor();
        byte[] byteTransitEntry = MessageHandler.objectToByteArray(this.transitRecord.getLastElement());

        if (compareTimestamp(message.getTimestamp(), timeOffset)) {
            try {
                if (ASAPCryptoAlgorithms.verify(signedTransferorField, byteTransitEntry, sender, sharkPKIComponent)) {
                    this.transitRecord = message.getTransitRecord();
                }
            } catch (ASAPSecurityException e) {
                throw new RuntimeException(e);
            }
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
        if (compareTimestamp(ackMessage.getTimestamp(), timeOffset) && ackMessage.getIsAck()) {
            return Optional.of(ackMessage);
        }
        return Optional.empty();
    }
}
