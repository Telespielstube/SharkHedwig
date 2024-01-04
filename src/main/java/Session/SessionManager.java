package Session;

import DeliveryContract.DeliveryContract;
import DeliveryContract.IDeliveryContract;
import DeliveryContract.ShippingLabel;
import Misc.LogEntry;
import Misc.Utilities;
import Session.Sessions.*;
import Setup.Channel;
import Setup.DeviceState;
import Message.*;
import net.sharksystem.asap.ASAPPeer;
import net.sharksystem.pki.SharkPKIComponent;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class SessionManager implements ISessionManager {

    private SessionState sessionState;
    private DeviceState deviceState;
    private AbstractSession identification;
    private AbstractSession request;
    private AbstractSession contract;
    private String sender;
    private Advertisement advertisement;
    private LogEntry logEntry;
    private MessageBuilder messageBuilder;
    private final IDeliveryContract shippingLabel = new ShippingLabel();
    private boolean noSession = false; // attribute because NoSession has no Session Object.
    private DeliveryContract deliveryContract;
    private Optional<Object> messageObject;

    public SessionManager() {}

    public SessionManager(MessageHandler messageHandler, SessionState sessionState, DeviceState deviceState, ASAPPeer peer, SharkPKIComponent sharkPKIComponent) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.deviceState = deviceState;
        this.sessionState = sessionState;
        this.identification = new Identification(sharkPKIComponent);
        this.request = new Request();
        this.contract = new Contract(messageHandler, sharkPKIComponent);
        this.messageObject = Optional.empty();
    }

    /**
     * A small message just to advertise a delivery service. Does not belong to a session.
     *
     * @return    Advertisement message object.
     */
    private Advertisement createAdvertisement() {
        return new Advertisement(Utilities.createUUID(), MessageFlag.Advertisement, Utilities.createTimestamp(), true);
    }

    @Override
    public void checkDeviceState() {
        if (this.shippingLabel.getIsCreated() ) {
            this.deviceState = DeviceState.Transferor.isActive();
        }
        this.deviceState = DeviceState.Transferee.isActive();
    }

    @Override
    public Optional<MessageBuilder> sessionHandling(IMessage message, String sender) {
        checkDeviceState();
        switch (this.sessionState) {
            case NoSession:
                if (this.deviceState.equals(DeviceState.Transferor.isActive())) {
                    this.messageObject = Optional.ofNullable(this.identification.transferor(message, sender).orElse(this.sessionState.resetSessionState()));
                }
                // NoSession and Transferee state only creates an Advertisement message.
                this.messageObject = Optional.of((createAdvertisement()));
                this.noSession = true;
                this.sessionState = SessionState.NoSession.nextState();
                messageBuilder = Optional.of(new MessageBuilder(this.messageObject.get(), Channel.Advertisement.getChannelType(), sender)).get();
                break;

            case Identification:
                if (!this.noSession) {
                    this.messageObject = Optional.empty();
                } else {
                    processIdentification(message);
                }
                if (this.messageObject.isPresent()) {
                    messageBuilder = Optional.of(new MessageBuilder(this.messageObject.get(), Channel.Identification.getChannelType(), sender)).get();
                }
                break;

            case Request:
                if (!this.noSession && !this.identification.getSessionComplete()) {
                    this.messageObject = Optional.empty();
                } else {
                    processRequest(message);
                }
                if (this.messageObject.isPresent()) {
                    messageBuilder = new MessageBuilder(this.messageObject.get(), Channel.Request.getChannelType(), sender);
                }
                break;

            case Contract:
                if (!this.noSession && !this.identification.getSessionComplete() && !this.request.getSessionComplete()) {
                    this.messageObject = Optional.empty();
                } else {
                    processContract(message);
                }
                if (this.messageObject.isPresent()) {
                    messageBuilder = new MessageBuilder(this.messageObject.get(), Channel.Contract.getChannelType(), sender);
                }
                break;

            default:
                System.err.println("There was no session flag: " + this.sessionState);
                resetAll();
                break;
        }
        return Optional.ofNullable(messageBuilder);
    }

    /**
     * If the previous session is completed the received identification message gets processed.
     *
     * @param message    Received identification message
     */
    private void processIdentification(IMessage message) {
        if (this.deviceState.equals(DeviceState.Transferor.isActive())) {
            this.messageObject = Optional.ofNullable(this.identification.transferor(message, sender).orElse(this.sessionState.resetSessionState()));
        } else {
            this.messageObject = Optional.ofNullable(this.identification.transferee(message, sender).orElse(this.sessionState.resetSessionState()));
        }
        if (this.messageObject.isPresent() && this.identification.setSessionComplete(this.messageObject)) {
            this.identification.clearMessageList();
            this.sessionState = SessionState.Identification.nextState();
        }
        this.messageObject = Optional.empty();
    }

    /**
     * If the previous session is completed the received request message gets processed.
     *
     * @param message    Received request message
     */
    private void processRequest(IMessage message) {
        if (this.deviceState.equals(DeviceState.Transferor.isActive())) {
            this.messageObject = Optional.ofNullable(this.request.transferor(message, sender).orElse(this.sessionState.resetSessionState()));
        } else {
            this.messageObject = Optional.ofNullable(this.request.transferee(message, sender).orElse(this.sessionState.resetSessionState()));
        }
        if (this.messageObject.isPresent() && this.request.setSessionComplete(this.messageObject)) {
            this.request.clearMessageList();
            this.sessionState = SessionState.Request.nextState();
        }
        this.messageObject = Optional.empty();
    }

    /**
     * If the previous session is completed the received contract message gets processed.
     *
     * @param message    Received contract message
     */
    private void processContract(IMessage message) {
        if (deviceState.equals(DeviceState.Transferor.isActive())) {
            this.messageObject = Optional.ofNullable(this.contract.transferor(message, sender).orElse(this.sessionState.resetSessionState()));
        } else {
            this.messageObject = Optional.ofNullable(this.contract.transferee(message, sender).orElse(this.sessionState.resetSessionState()));
        }
        if (this.messageObject.isPresent() && this.contract.setSessionComplete(this.messageObject)) {
            changeDeviceState();
            resetAll();
        }
        this.messageObject = Optional.empty();
    }
    /**
     * After the contract log is written it is assumed that the package is exchanged. Therefore, the states must switch
     * vice versa= and the ShippingLabel state must change as well.
     */
    public void changeDeviceState() {
        if (deviceState.equals(DeviceState.Transferor)) {
            deviceState = DeviceState.Transferee.isActive();
            this.deliveryContract.resetContractState();
        } else { // New transferor does not have to set the contract state. Already set in 'storeDeliveryContract' methode.
            deviceState = DeviceState.Transferor.isActive();
        }
    }

    /**
     * Method to reset every list and current state, but not the ShippingLabel state!!!
     */
    public void resetAll() {
        this.noSession = false;
        this.identification.setSessionComplete(false);
        this.request.setSessionComplete(false);
        this.contract.setSessionComplete(false);
        this.identification.clearMessageList();
        this.request.clearMessageList();
        this.contract.clearMessageList();
        this.sessionState.resetSessionState();
        this.deliveryContract.setContractSent(false);
    }
}
