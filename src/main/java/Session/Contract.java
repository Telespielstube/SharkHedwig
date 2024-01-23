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
    private Optional<Message> optionalMessage;

    public Contract(SharkPKIComponent sharkPKIComponent) {
        this.sharkPKIComponent = sharkPKIComponent;
        this.messageList = new TreeMap<>();
        this.contractState = false;
        this.geoSpatial = new GeoSpatial();
        this.optionalMessage = Optional.empty();
    }

    @Override
    public Optional<Object> transferor(IMessage message, String sender) {

        // Check object state to make sure to send the contract documents only once.
        if (!this.contractState) {
            createDeliveryContract(sender);
        } else if (this.deliveryContract.getTransitRecord().getListSize() > 1
                && !this.deliveryContract.getTransitRecord().getLastElement().getTransferor().equals(AppConstant.PEER_NAME)) {
            updateTransitRecord(sender);
        }

        switch(message.getMessageFlag()) {
            case CONFIRM:
                handleConfirm((Confirm) message, sender).orElse(null));
                break;
            case ACK:
                handleAckMessage((AckMessage) message);
                if (this.optionalMessage.isPresent()) {
                    Logger.writeLog(this.deliveryContract.getString(), AppConstant.DELIVERY_CONTRACT_LOG_PATH.toString()
                            + this.deliveryContract.getShippingLabel().getUUID());
                }
                break;
            case COMPLETE:
                handleCompleteMessage((Complete) message);
                break;
            default:
                System.err.println("Message flag was incorrect: " + message.getMessageFlag());
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

    @Override
    public Optional<Object> transferee(IMessage message, String sender) {

        switch(message.getMessageFlag()) {
            case CONTRACT_DOCUMENT:
                handleContract((ContractDocument) message).orElse(null);
                break;
            case PICK_UP:
                handlePickUp((PickUp) message, sender);
                if (this.optionalMessage.isPresent()) {
                    Logger.writeLog(this.deliveryContract.toString(), AppConstant.DELIVERY_CONTRACT_LOG_PATH.toString()
                            + this.deliveryContract.getShippingLabel().getUUID());
                }
                break;
            case READY:
                handleAckMessage((AckMessage) message);
                break;
            default:
                System.err.println("Message flag was incorrect: " + message.getMessageFlag());
                clearMessageList();
                break;
        }
        if (!this.optionalMessage.isPresent()) {
            clearMessageList();
        } else {
            addMessageToList(this.optionalMessage.get());
        }this.
        return Optional.of(this.optionalMessage);
    }

    /**
     * Creates the contract document object. The ShippingLabel object is already in memory and the TransitRecord object
     * gets created now.
     *
     * @param receiver   The sender of the message is the receiver of the newly created DeliveryContract object.
     *
     * @return           new ContractDocument message object.
     */
    private void createDeliveryContract(String receiver) {
        this.deliveryContract = new DeliveryContract(receiver, geoSpatial);
        this.contractState = ContractState.CREATED.getState();
        this.optionalMessage = Optional.of(new ContractDocument(Utilities.createUUID(), MessageFlag.CONTRACT_DOCUMENT, Utilities.createTimestamp(), this.deliveryContract));
    }

    /**
     * Updates the TransitRecord object. This method is called after the former Transferee and current Transferor needs
     * to send the DeliveryContract to the next Transferee device.
     *
     * @param receiver    The receiver of the updated TransitRecord object.
     *
     * @return            ContractDocument.
     */
    private void updateTransitRecord(String receiver) {
        TransitEntry update = new TransitEntry(this.deliveryContract.getTransitRecord().countUp(),
                this.deliveryContract.getShippingLabel().getUUID(),
                AppConstant.PEER_NAME.toString(),
                receiver, geoSpatial.getCurrentLocation(),
                Utilities.createTimestamp(),
                null,
                null);
        this.deliveryContract.getTransitRecord().addEntry(update);
        this.optionalMessage = Optional.of(new ContractDocument(Utilities.createUUID(), MessageFlag.CONTRACT_DOCUMENT, Utilities.createTimestamp(), this.deliveryContract));
    }

    /**
     * Validates the received Confirm message object and verifies the signed transferee field.
     *
     * @param message    Confirm messge object.
     * @return           PickUp message, empty Optional if time or signature is not correct.
     */
    private void handleConfirm(Confirm message, String sender) {
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
            this.optionalMessage = Optional.of(new PickUp(Utilities.createUUID(), MessageFlag.PICK_UP, Utilities.createTimestamp(), this.transitRecord));
        }
    }

    /**
     * Handles all things data processing after receiving contract documents.
     *
     * @param message    PickUp message object.
     * @return           An optional if the message passed the timestamp and flag checks or empty if not.
     */
    private void handleContract(ContractDocument message) {
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
            this.optionalMessage = Optional.of(new Confirm(Utilities.createUUID(), MessageFlag.CONFIRM, Utilities.createTimestamp(), this.deliveryContract, true));
        }
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
    private void handlePickUp(PickUp message, String sender) {
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
            this.optionalMessage = Optional.of(new AckMessage(Utilities.createUUID(), MessageFlag.ACK, Utilities.createTimestamp(), true));
        }
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
            this.optionalMessage = Optional.of(new AckMessage(Utilities.createUUID(), MessageFlag.READY, Utilities.createTimestamp(), true));
        }
    }

    /**
     * The Complete message is sent by the transferee and marks the completion of the transaction.
     *
     * @param message    Complete message object.
     *
     * @return           Optional object if message is validated correctly, empty if not.
     */
    private void handleCompleteMessage(Complete message) {
        timeOffset = 30000;
        if (compareTimestamp(message.getTimestamp(), timeOffset) && message.getComplete()) {
            // Send a message to the owner of the drone that a package is handed over to another drone.
            //notificationService.newMessage(DeliveryContract deliveryContract);
            this.optionalMessage =  Optional.of(message);
        } else {
            // Send a message to the drone owner that a package is lost
            //notificationService.newMessage(DeliveryContract deliveryContract);
        }
    }
}
