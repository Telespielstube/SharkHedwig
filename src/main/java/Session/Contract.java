package Session;

import DeliveryContract.*;
import Location.*;
import Message.Message;
import Message.Contract.*;
import Message.IMessage;
import Message.MessageFlag;
import Message.MessageHandler;
import Misc.Logger;
import Misc.Utilities;
import Setup.AppConstant;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.asap.crypto.ASAPCryptoAlgorithms;
import net.sharksystem.pki.SharkPKIComponent;

import java.util.*;

public class Contract extends AbstractSession {

    private final SharkPKIComponent sharkPKIComponent;
    private DeliveryContract deliveryContract;
    private final IGeoSpatial geoSpatial;
    private TransitRecord transitRecord;
    private byte[] signedField;
    private boolean contractState = false;

    public Contract(SharkPKIComponent sharkPKIComponent) {
        this.sharkPKIComponent = sharkPKIComponent;
        this.messageList = new TreeMap<>();
        this.contractState = false;
        this.geoSpatial = new GeoSpatial();
    }

    @Override
    public Optional<Object> transferor(IMessage message, String sender) {
        Optional<Message> messageObject = Optional.empty();
        // Check object state to make sure to send the contract documents only once.
        if (!this.contractState) {
            messageObject = Optional.of(createDeliveryContract(sender));
        } else if (this.deliveryContract.getTransitRecord().getListSize() > 1
                && !this.deliveryContract.getTransitRecord().getLastElement().getTransferor().equals(AppConstant.PEER_NAME)) {
            messageObject = Optional.of(updateTransitRecord(sender));
        }

        switch(message.getMessageFlag()) {
            case CONFIRM:
                messageObject = Optional.ofNullable(handleConfirm((Confirm) message, sender).orElse(null));
                break;
            case ACK:
                messageObject = Optional.ofNullable(handleAckMessage((AckMessage) message).orElse(null));
                if (messageObject.isPresent()) {
                    Logger.writeLog(this.deliveryContract.getString(), AppConstant.DELIVERY_CONTRACT_LOG_PATH.toString() +
                            this.deliveryContract.getShippingLabel().getUUID());
                }
                break;
            case COMPLETE:
                messageObject = Optional.ofNullable(handleCompleteMessage((Complete) message).orElse(null));
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
        Optional<Message> messageObject = Optional.empty();
        switch(message.getMessageFlag()) {
            case CONTRACT_DOCUMENT:
                messageObject = Optional.ofNullable(handleContract((ContractDocument) message).orElse(null));
                break;
            case PICK_UP:
                messageObject = Optional.ofNullable(handlePickUp((PickUp) message, sender).orElse(null));
                if (messageObject.isPresent()) {
                    Logger.writeLog(this.deliveryContract.toString(), AppConstant.DELIVERY_CONTRACT_LOG_PATH.toString() +
                            this.deliveryContract.getShippingLabel().getUUID());
                }
                break;
            case READY:
                messageObject = Optional.ofNullable(handleAckMessage((AckMessage) message).orElse(null));
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
     * @param receiver   The sender of the message is the receiver of the newly created DeliveryContract object.
     *
     * @return           new ContractDocument message object.
     */
    private ContractDocument createDeliveryContract(String receiver) {
        this.deliveryContract = new DeliveryContract(receiver, geoSpatial);
        this.contractState = ContractState.CREATED.getState();
        return new ContractDocument(Utilities.createUUID(), MessageFlag.CONTRACT_DOCUMENT,
                Utilities.createTimestamp(), this.deliveryContract);
    }

    /**
     * Updates the TransitRecord object. This method is called after the former Transferee and current Transferor needs
     * to send the DeliveryContract to the next Transferee device.
     *
     * @param receiver    The receiver of the updated TransitRecord object.
     *
     * @return            ContractDocument.
     */
    private ContractDocument updateTransitRecord(String receiver) {
        TransitEntry update = new TransitEntry(this.deliveryContract.getTransitRecord().countUp(),
                this.deliveryContract.getShippingLabel().getUUID(),
                AppConstant.PEER_NAME.toString(),
                receiver, geoSpatial.getCurrentLocation(),
                Utilities.createTimestamp(),
                null,
                null);
        this.deliveryContract.getTransitRecord().addEntry(update);
        return new ContractDocument(Utilities.createUUID(), MessageFlag.CONTRACT_DOCUMENT, Utilities.createTimestamp(),
               this.deliveryContract);
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
            try {
                if (ASAPCryptoAlgorithms.verify(signedTransfereeField, byteTransitEntry, sender, this.sharkPKIComponent.getASAPKeyStore())) {
                    this.signedField = ASAPCryptoAlgorithms.sign(byteTransitEntry, this.sharkPKIComponent);
                    this.transitRecord.getLastElement().setSignatureTransferor(this.signedField);
                }
            } catch (ASAPSecurityException e) {
                System.err.println("Caught an ASAPSecurityException: " + e);
                throw new RuntimeException(e);
            }
            return Optional.of(new PickUp(Utilities.createUUID(), MessageFlag.PICK_UP, Utilities.createTimestamp(), this.transitRecord));
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
            this.transitRecord = message.getDeliveryContract().getTransitRecord();
            this.geoSpatial.setPickUpLocation(this.transitRecord.getLastElement().getPickUpLocation());
            try {
                this.signedField = ASAPCryptoAlgorithms.sign(MessageHandler.objectToByteArray(this.transitRecord.getLastElement()), sharkPKIComponent);
                this.transitRecord.getLastElement().setSignatureTransferee(this.signedField);
            } catch (ASAPSecurityException e) {
                System.err.println("Caught an ASAPSecurityException: " + e);
                throw new RuntimeException(e);
            }
            inMemoDeliveryContract(message.getDeliveryContract());
            return Optional.of(new Confirm(Utilities.createUUID(), MessageFlag.CONFIRM, Utilities.createTimestamp(), this.deliveryContract, true));
        }
        return Optional.empty();
    }

    /**
     * The transferee needs to store the DeliveryContract in memory until the transferor signed the transit record entry too.
     *
     * @param message    The DeliveryContract object.
     */
    private void inMemoDeliveryContract(DeliveryContract message) {
        ShippingLabel label = message.getShippingLabel();
        this.deliveryContract = new DeliveryContract(new ShippingLabel(label.getUUID(), label.getSender(), label.getOrigin(),
                label.getPackageOrigin(), label.getRecipient(), label.getDestination(), label.getPackageDestination(),
                label.getPackageWeight()), new TransitRecord(this.transitRecord.getAllEntries()));
        this.contractState = ContractState.CREATED.getState();
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
            return Optional.of(new AckMessage(Utilities.createUUID(), MessageFlag.ACK, Utilities.createTimestamp(), true));
        }
        return Optional.empty();
    }

    /**
     * An Acknowledgment message to signal that the PickUpMessage was received.
     *
     * @param nessage    The received AckMessage object.
     * @return              An optional AckMessage object if timestamp and ack flag are ok
     *                      or an empty Optional if its not.
     */
    private Optional<AckMessage> handleAckMessage(AckMessage nessage)  {
        if (compareTimestamp(nessage.getTimestamp(), timeOffset) && nessage.getIsAck()) {
            return Optional.of(new AckMessage(Utilities.createUUID(), MessageFlag.READY, Utilities.createTimestamp(), true));
        }
        return Optional.empty();
    }

    /**
     * The Complete message is sent by the transferee and marks the completion of the transaction.
     *
     * @param message    Complete message object.
     *
     * @return           Optional object if message is validated correctly, empty if not.
     */
    private Optional<Complete> handleCompleteMessage(Complete message) {
        timeOffset = 30000;
        if (compareTimestamp(message.getTimestamp(), timeOffset) && message.getComplete()) {
            // Send a message to the owner of the drone that a package is handed over to another drone.
            //notificationService.newMessage(DeliveryContract deliveryContract);
            return Optional.of(message);
        } else {
            // Send a message to the drone owner that a package is lost
            //notificationService.newMessage(DeliveryContract deliveryContract);
        }
        return Optional.empty();
    }
}
