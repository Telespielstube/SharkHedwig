package ProtocolRole.State;

import DeliveryContract.ShippingLabel;
import Message.Contract.Complete;
import Message.Contract.PickUp;
import Message.MessageFlag;
import Message.MessageHandler;
import Message.Messageable;
import Message.Request.Ack;
import Message.Request.Confirm;
import Message.Request.Offer;
import Message.Request.OfferReply;
import Misc.LogEntry;
import Misc.Logger;
import Misc.Utilities;
import ProtocolRole.ProtocolRole;
import Setup.AppConstant;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.asap.crypto.ASAPCryptoAlgorithms;

import java.util.Optional;

public class Transferor implements ProtocolState {

    private final ProtocolRole protocolRole;

    public Transferor(ProtocolRole protocolRole) {
        this.protocolRole = protocolRole;
    }

    @Override
    public void handle(Messageable message, String sender) {
        //this.protocolRole.setProtocolState(this.protocolRole.getTransferorState());
        switch (message.getMessageFlag()) {
            case OFFER:
                handleOffer((Offer) message, shippingLabel);
                break;
            case CONFIRM:
                handleConfirm((Confirm) message);
                break;
            case READY:
                handleAck((Ack) message);
                saveData();
                break;
            case CONFIRM:
                handleConfirmMessage((Message.Contract.Confirm) message, sender);
                break;
            case ACK:
                handleAckMessage((Message.Contract.Ack) message);
                saveData();
                break;
            case COMPLETE:
                handleCompleteMessage((Complete) message);
                break;
            default:
                System.err.println(Utilities.formattedTimestamp() + "Missing message flag.");
                this.receivedMessageList.clearMessageList();
                break;
        }
        if (!this.optionalMessage.isPresent()) {
            this.receivedMessageList.clearMessageList();
        } else {
            this.receivedMessageList.addMessageToList(this.optionalMessage.get());
        }
        return this.optionalMessage;
    }

    @Override
    public void changeRole() {
        this.protocolRole.setProtocolState(this.protocolRole.getTranfereeState());
    }

    /**
     * Processes the Offer data received from the Transferee.
     *
     * @param message Offer message object.
     * @return OfferReply object, or and enpty object if data were not verified.
     */
    private void handleOffer(Offer message, ShippingLabel shippingLabel) {
        if (this.receivedMessageList.compareTimestamp(message.getTimestamp(), this.timeOffset) && verifyOfferData(message)) {
            if (processOfferData(message)) {
                this.optionalMessage = Optional.of(new OfferReply(Utilities.createUUID(), MessageFlag.OFFER_REPLY, Utilities.createTimestamp(), shippingLabel.get().getPackageWeight(), shippingLabel.get().getPackageDestination()));
            }
        }
    }

    /**
     * Validates the received Confirm message object.
     *
     * @param message Confirm messge object.
     * @return An empty optional if a Confirm object is found.
     */
    private void handleConfirm(Confirm message) {
        if (this.receivedMessageList.compareTimestamp(message.getTimestamp(), this.timeOffset) && message.getConfirmed()) {
            this.optionalMessage = Optional.of(new Ack(Utilities.createUUID(), MessageFlag.ACK, Utilities.createTimestamp(), true));
        }
    }

    /**
     * An Acknowledgment massage to signal that the second secure code was decrypted successfully. It compares the timestamp
     * checks the flag and checks if the last TreeMap entry equals Ack.
     *
     * @param message The received AckMessage object.
     * @return An Optional AckMessage object if timestamp and ack flag are ok
     * or an empty Optional if it's not.
     */
    private void handleAck(Ack message) {
        if (this.receivedMessageList.compareTimestamp(message.getTimestamp(), timeOffset) && message.getIsAck()) {
            if (message.getMessageFlag().equals(MessageFlag.ACK)) {
                this.optionalMessage = Optional.of(new Ack(Utilities.createUUID(), MessageFlag.READY, Utilities.createTimestamp(), true));
                this.sessionComplete = true;
            } else if (!message.getMessageFlag().equals(MessageFlag.READY)) {
                this.optionalMessage = this.contract.checkContractState(this.sender);
                this.sessionComplete = true;
            }
        }
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

    /**
     * Validates the received Confirm message object verifies the signed transferee field and fills in the last field
     * by signing the element.
     *
     * @param message    Confirm messge object.
     */
    private void handleConfirmMessage(Message.Contract.Confirm message, String sender) {
        if (this.receivedMessageList.compareTimestamp(message.getTimestamp(), this.timeOffset) && message.getConfirmed()) {
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
     * An Acknowledgment massage to signal that the second secure code was decrypted successfully. It compares the timestamp
     * checks the flag and checks if the last TreeMap entry equals Ack.
     *
     * @param message    The received AckMessage object.
     */
    private void handleAckMessage(Message.Contract.Ack message)  {
        if (this.receivedMessageList.compareTimestamp(message.getTimestamp(), timeOffset)) {
            if (message.getIsAck()) {
                this.optionalMessage = Optional.of(new Message.Contract.Ack(Utilities.createUUID(), MessageFlag.READY,
                        Utilities.createTimestamp(), true));
                this.sessionComplete = true;
            } else if (!message.getMessageFlag().equals(MessageFlag.READY)) {
                this.optionalMessage = Optional.of(new Message.Contract.Ack(Utilities.createUUID(), MessageFlag.COMPLETE,
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
        if (this.receivedMessageList.compareTimestamp(message.getTimestamp(), timeOffset) && message.getComplete()) {
            // Send a message to the owner of the drone that a package is handed over to another drone.
            //notificationService.newMessage(DeliveryContract deliveryContract);
            this.sessionComplete = true;
        } else {
            // Send a message to the drone owner that a package is lost
            //notificationService.newMessage(DeliveryContract deliveryContract);
        }
    }
}
