package Session;

import DeliveryContract.*;
import Location.*;
import Message.Message;
import Message.Contract.*;
import Message.Messageable;
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

    private SharkPKIComponent sharkPKIComponent;
    private DeliveryContract deliveryContract;
    private Locationable geoSpatial;
    private TransitRecord transitRecord;
    private byte[] signedField;
    private boolean contractState = false;
    private Optional<Message> optionalMessage;
    private MessageList messageList;

    public Contract(SharkPKIComponent sharkPKIComponent, MessageList messageList) {
        this.sharkPKIComponent = sharkPKIComponent;
        this.messageList = messageList;
        this.contractState = false;
        this.geoSpatial = new GeoSpatial();
        this.optionalMessage = Optional.empty();
    }

    @Override
    public Optional<Message> transferor(Messageable message, ShippingLabel shippingLabel, String sender) {
        switch(message.getMessageFlag()) {
            case CONFIRM:
                handleConfirm((Confirm) message, sender);
                break;
            case ACK:
                handleAckMessage((Ack) message);
                saveData();
                break;
            case COMPLETE:
                handleCompleteMessage((Complete) message);
                break;
            default:
                System.err.println(Utilities.formattedTimestamp() + "Message flag was incorrect: " + message.getMessageFlag());
                this.messageList.clearMessageList();
                break;
        }
        if (!this.optionalMessage.isPresent()) {
            this.messageList.clearMessageList();
        } else {
            this.messageList.addMessageToList(this.optionalMessage.get());
        }
        return Optional.of(this.optionalMessage.get());
    }

    @Override
    public Optional<Message> transferee(Messageable message, String sender) {

        switch(message.getMessageFlag()) {
            case CONTRACT_DOCUMENT:
                handleContract((ContractDocument) message);
                break;
            case PICK_UP:
                handlePickUp((PickUp) message, sender);
                saveData();
                break;
            case READY:
                handleAckMessage((Ack) message);
                break;
            default:
                System.err.println(Utilities.formattedTimestamp() + "Message flag was incorrect: " + message.getMessageFlag());
                this.messageList.clearMessageList();
                break;
        }
        if (!this.optionalMessage.isPresent()) {
            this.messageList.clearMessageList();
        } else {
            this.messageList.addMessageToList(this.optionalMessage.get());
        }
        return Optional.of(this.optionalMessage.get());
    }

    /**
     * Checks the state of the contract. This method is called from the request session. To initiate the contract session
     * and sending the correct contract.
     *
     * @return    Optional message ContractDocument containing the DeliveryContract.
     */
    public Optional<Message> checkContractState(String sender) {
        // Check object state to make sure to send the contract documents only once.
        if (!this.contractState) {
            createDeliveryContract(sender);
        } else if (this.deliveryContract.getTransitRecord().getListSize() > 1
                && !this.deliveryContract.getTransitRecord().getLastElement().getTransferor().equals(AppConstant.PEER_NAME)) {
            updateTransitRecord(sender);
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
     * Updates the TransitRecord object. This method is called after the former Transferee and current Transferor needs
     * to send the DeliveryContract to the next Transferee device.
     *
     * @param receiver    The receiver of the updated TransitRecord object.
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
     * Validates the received Confirm message object verifies the signed transferee field and fills in the last field
     * by signing the element.
     *
     * @param message    Confirm messge object.
     */
    private void handleConfirm(Confirm message, String sender) {
        if (this.messageList.compareTimestamp(message.getTimestamp(), this.timeOffset) && message.getConfirmed()) {
            byte[] signedTransfereeField = message.getDeliveryContract().getTransitRecord().getLastElement().getSignatureTransferee();
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
     * Handles all things data processing after receiving contract documents.
     *
     * @param message    PickUp message object.
     */
    private void handleContract(ContractDocument message) {
        if (message.getDeliveryContract() != null) {
            this.transitRecord = message.getDeliveryContract().getTransitRecord();
            this.geoSpatial.setPickUpLocation(this.transitRecord.getLastElement().getPickUpLocation());
            try {
                this.signedField = ASAPCryptoAlgorithms.sign(MessageHandler.objectToByteArray(this.transitRecord.getLastElement()), sharkPKIComponent);
                this.transitRecord.getLastElement().setSignatureTransferee(this.signedField);
            } catch (ASAPSecurityException e) {
                System.err.println(Utilities.formattedTimestamp() + "Caught an ASAPSecurityException: " + e.getMessage());
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
        this.deliveryContract = new DeliveryContract(new ShippingLabel.Builder(label.getUUID(), label.getSender(), label.getOrigin(),
                label.getPackageOrigin(), label.getRecipient(), label.getDestination(), label.getPackageDestination(),
                label.getPackageWeight()).build(), new TransitRecord(this.transitRecord.getAllEntries()));
        this.contractState = ContractState.CREATED.getState();
    }

    /**
     * The PickUp message is sent when the package is ready to pickup.
     *
     * @param message    PickUp message object.
     */
    private void handlePickUp(PickUp message, String sender) {
        byte[] signedTransferorField = message.getTransitRecord().getLastElement().getSignatureTransferor();
        byte[] byteTransitEntry = MessageHandler.objectToByteArray(this.transitRecord.getLastElement());

        if (this.messageList.compareTimestamp(message.getTimestamp(), timeOffset)) {
            try {
                if (ASAPCryptoAlgorithms.verify(signedTransferorField, byteTransitEntry, sender, sharkPKIComponent)) {
                    this.transitRecord = message.getTransitRecord();
                }
            } catch (ASAPSecurityException e) {
                throw new RuntimeException(e);
            }
            this.optionalMessage = Optional.of(new Ack(Utilities.createUUID(), MessageFlag.ACK, Utilities.createTimestamp(), true));
        }
    }

    /**
     * An Acknowledgment massage to signal that the second secure code was decrypted successfully. It compares the timestamp
     * checks the flag and checks if the last TreeMap entry equals Ack.
     *
     * @param message    The received AckMessage object.
     */
    private void handleAckMessage(Ack message)  {
        if (this.messageList.compareTimestamp(message.getTimestamp(), timeOffset)) {
            if (message.getIsAck()) {
                this.optionalMessage = Optional.of(new Ack(Utilities.createUUID(), MessageFlag.READY,
                        Utilities.createTimestamp(), true));
                this.sessionComplete = true;
            } else if (!message.getMessageFlag().equals(MessageFlag.READY)) {
                this.optionalMessage = Optional.of(new Ack(Utilities.createUUID(), MessageFlag.COMPLETE,
                        Utilities.createTimestamp(), true));
                this.sessionComplete = true;
            }
        }
    }

    /**
     * The Complete message is sent by the transferee and marks the completion of the transaction.
     *
     * @param message    Complete message object.
     */
    private void handleCompleteMessage(Complete message) {
        timeOffset = 30000;
        if (this.messageList.compareTimestamp(message.getTimestamp(), timeOffset) && message.getComplete()) {
            // Send a message to the owner of the drone that a package is handed over to another drone.
            //notificationService.newMessage(DeliveryContract deliveryContract);
            this.sessionComplete = true;
        } else {
            // Send a message to the drone owner that a package is lost
            //notificationService.newMessage(DeliveryContract deliveryContract);
        }
    }

    /**
     * Saves the important session data to the give path constant.
     */
    private void saveData() {
        if (this.optionalMessage.isPresent()) {
            String saveFile = AppConstant.DELIVERY_CONTRACT_LOG_PATH.toString()
                    + this.deliveryContract.getShippingLabel().getUUID();
            Logger.writeLog(this.deliveryContract.toString(), saveFile);
        }
    }
}
