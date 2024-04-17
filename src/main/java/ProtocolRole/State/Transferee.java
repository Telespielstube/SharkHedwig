package ProtocolRole.State;

import DeliveryContract.DeliveryContract;
import DeliveryContract.ShippingLabel;
import Message.Contract.ContractDocument;
import Message.Contract.PickUp;
import Message.MessageFlag;
import Message.MessageHandler;
import Message.Messageable;
import Message.Request.Ack;
import Message.Request.Confirm;
import Message.Request.Offer;
import Message.Request.OfferReply;
import Message.NoSession.Solicitation;
import Misc.Logger;
import Misc.Utilities;
import ProtocolRole.ProtocolRole;
import Setup.AppConstant;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.asap.crypto.ASAPCryptoAlgorithms;

import java.util.Optional;

public class Transferee implements ProtocolState{

    private final ProtocolRole protocolRole;

    public Transferee(ProtocolRole protocolRole) {
        this.protocolRole = protocolRole;
    }

    @Override
    public void handle(Messageable message, String sender) {
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
            case CONTRACT_DOCUMENT:
                handleContract((ContractDocument) message);
                break;
            case PICK_UP:
                handlePickUp((PickUp) message, sender);
                saveData();
                break;
            case READY:
                handleAckMessage((Message.Contract.Ack) message);
                break;
            default:
                System.err.println(Utilities.formattedTimestamp() + "Message flag was incorrect: " + message.getMessageFlag());
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
        this.protocolRole.setProtocolState(this.protocolRole.getTransferorState());
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
            this.optionalMessage = Optional.of(new Message.Contract.Confirm(Utilities.createUUID(), MessageFlag.CONFIRM, Utilities.createTimestamp(), this.deliveryContract, true));
        }
    }

    /**
     * The incoming solicitation message gives the signal to the transferee to create and send out the offer message to the
     * transeror drone.
     *
     * @param message    The received Solicitation message.
     */
    private void handleSolicitation(Solicitation message) {
        if (this.receivedMessageList.compareTimestamp(message.getTimestamp(), this.timeOffset) && message.getSolicitate()) {
            this.optionalMessage = Optional.of(new Offer(Utilities.createUUID(), MessageFlag.OFFER, Utilities.createTimestamp(),
                    this.battery.getCurrentBatteryLevel(), AppConstant.MAX_FREIGHT_WEIGHT.getInt(), this.geoSpatial.getCurrentLocation()));
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
        if (this.receivedMessageList.compareTimestamp(message.getTimestamp(), this.timeOffset) && processOfferReplyData(message)) {
            this.optionalMessage = Optional.of(new Confirm(Utilities.createUUID(), MessageFlag.CONFIRM, Utilities.createTimestamp(), true));
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
        if (this.receivedMessageList.compareTimestamp(message.getTimestamp(), timeOffset) && message.getIsAck()) {
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
     * The PickUp message is sent when the package is ready to pickup.
     *
     * @param message    PickUp message object.
     */
    private void handlePickUp(PickUp message, String sender) {
        byte[] signedTransferorField = message.getTransitRecord().getLastElement().getSignatureTransferor();
        byte[] byteTransitEntry = MessageHandler.objectToByteArray(this.transitRecord.getLastElement());

        if (this.receivedMessageList.compareTimestamp(message.getTimestamp(), timeOffset)) {
            try {
                if (ASAPCryptoAlgorithms.verify(signedTransferorField, byteTransitEntry, sender, sharkPKIComponent)) {
                    this.transitRecord = message.getTransitRecord();
                }
            } catch (ASAPSecurityException e) {
                throw new RuntimeException(e);
            }
            this.optionalMessage = Optional.of(new Message.Contract.Ack(Utilities.createUUID(), MessageFlag.ACK, Utilities.createTimestamp(), true));
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
