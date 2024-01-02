package Session;

import DeliveryContract.DeliveryContract;
import DeliveryContract.IContractComponent;
import DeliveryContract.ShippingLabel;
import Misc.LogEntry;
import Misc.Utilities;
import Setup.Channel;
import Setup.DeviceState;
import Message.*;
import Session.Sessions.*;
import net.sharksystem.asap.ASAPPeer;
import net.sharksystem.pki.SharkPKIComponent;
import sun.awt.windows.ThemeReader;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.UUID;

public class SessionManager implements ISessionManager {

    private SessionState sessionState;
    private DeviceState deviceState;
    private Identification identification;
    private Request request;
    private Contract contract;
    private String sender;
    private Advertisement advertisement;
    private LogEntry logEntry;
    private MessageBuilder messageBuilder;
    private final IContractComponent shippingLabel = new ShippingLabel();
    private boolean noSession = false; // attribute because NoSession has no Session Object.
    private DeliveryContract deliveryContract;

    public SessionManager() {}

    public SessionManager(MessageHandler messageHandler, SessionState sessionState, DeviceState deviceState, ASAPPeer peer, SharkPKIComponent sharkPKIComponent) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.deviceState = deviceState;
        this.sessionState = sessionState;
        this.identification = new Identification(sharkPKIComponent);
        this.request = new Request();
        this.contract = new Contract(messageHandler, sharkPKIComponent);
    }

    /**
     * A small message just to advertise a delivery service. Does not belong to a session.
     *
     * @return    Advertisement message object.
     */
    private Advertisement createAdvertisement() {
        return new Advertisement(UUID.randomUUID(), MessageFlag.Advertisement, Utilities.createTimestamp(), true);
    }

    @Override
    // Second condition is necessary because the changeDeviceState() switches only deviceState not shippingLabel state.
    public boolean checkTransferorState() {
        if (shippingLabel.isCreated() || this.deviceState.equals(DeviceState.Transferor.isActive())) {
            this.deviceState = DeviceState.Transferor.isActive();
            return true;
        }
        this.deviceState = DeviceState.Transferee.isActive();
        return false;
    }

    @Override
    public MessageBuilder sessionHandling(IMessage message, String sender) {
        Optional<Object> messageObject = Optional.empty();
        switch (this.sessionState) {
            case NoSession:
                if (checkTransferorState()) {
                    messageObject = Optional.ofNullable(this.identification.transferor(message, sender).orElse(this.sessionState.resetSessionState()));
                }
                messageObject = Optional.of((createAdvertisement()));
                this.noSession = true;
                this.sessionState.nextState();
                messageBuilder = new MessageBuilder(messageObject.get(), Channel.Advertisement.getChannelType(), sender);
                break;

            case Identification:
                if (this.noSession) {
                    if (checkTransferorState()) {
                        messageObject = Optional.ofNullable(this.identification.transferor(message, sender).orElse(this.sessionState.resetSessionState()));
                    } else {
                        messageObject = Optional.ofNullable(this.identification.transferee(message, sender).orElse(this.sessionState.resetSessionState()));
                    }
                    if (messageObject.isPresent() && this.identification.sessionComplete(messageObject)) {
                        this.identification.setSessionComplete(true);
                        this.identification.clearMessageList();
                        this.sessionState.nextState();
                    }
                    messageObject = Optional.empty();
                }
                messageBuilder = new MessageBuilder(messageObject.get(), Channel.Identification.getChannelType(), sender);
                break;

            case Request:
                if (this.noSession && this.identification.getSessionComplete()) {
                    if (checkTransferorState()) {
                        messageObject = Optional.ofNullable(this.request.transferor(message, sender).orElse(this.sessionState.resetSessionState()));
                    } else {
                        messageObject = Optional.ofNullable(this.request.transferee(message, sender).orElse(this.sessionState.resetSessionState()));
                    }
                    if (messageObject.isPresent() && this.request.sessionComplete(messageObject)) {
                        this.request.setSessionComplete(true);
                        this.request.clearMessageList();
                        this.sessionState.nextState();
                    }
                    messageObject = Optional.empty();
                }
                messageBuilder = new MessageBuilder(messageObject.get(), Channel.Request.getChannelType(), sender);
                break;

            case Contract:
                if (this.noSession && this.identification.getSessionComplete() && this.request.getSessionComplete()) {
                    if (checkTransferorState()) {
                        messageObject = Optional.ofNullable(this.contract.transferor(message, sender).orElse(this.sessionState.resetSessionState()));
                    } else {
                        messageObject = Optional.ofNullable(this.contract.transferee(message, sender).orElse(this.sessionState.resetSessionState()));
                    }
                    if (messageObject.isPresent() && this.contract.sessionComplete(messageObject)) {
                        this.contract.setSessionComplete(true);
                        changeDeviceState();
                        resetAll();
                    }
                    messageObject = Optional.empty();
                }
                messageBuilder = new MessageBuilder(messageObject.get(), Channel.Contract.getChannelType(), sender);
                break;

            default:
                System.err.println("There was no session flag: " + this.sessionState);
                resetAll();
                break;
        }
        return messageBuilder;
    }

    /**
     * After the contract log is written it is assumed that the package is exchanged. Therefore, the states must switch
     * vice versa.
     */
    public void changeDeviceState() {
        if (deviceState.equals(DeviceState.Transferor)) {
            deviceState = DeviceState.Transferee.isActive();
        } else {
            deviceState = DeviceState.Transferor.isActive();
        }
    }

    /**
     * Method to reset every list and current state.
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
