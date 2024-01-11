package Session;

import DeliveryContract.DeliveryContract;
import DeliveryContract.ShippingLabel;
import Misc.Utilities;
import Session.Sessions.*;
import Setup.Channel;
import Setup.ProtocolState;
import Message.*;
import net.sharksystem.asap.ASAPPeer;
import net.sharksystem.pki.SharkPKIComponent;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class SessionManager implements ISessionManager {

    private SessionState sessionState;
    private ProtocolState protocolState;
    private AbstractSession identification;
    private AbstractSession request;
    private AbstractSession contract;
    private MessageBuilder messageBuilder;
    private DeliveryContract shippingLabel;
    private boolean noSession = false; // attribute because NoSession has no Session Object
    private Optional<Object> messageObject;
    private String sender;
    private DeliveryContract deliveryContract;

    public SessionManager(SessionState sessionState, ProtocolState protocolState, ASAPPeer peer, SharkPKIComponent sharkPKIComponent) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.protocolState = protocolState;
        this.sessionState = sessionState;
        this.identification = new Identification(sharkPKIComponent);
        this.shippingLabel = shippingLabel;
        this.request = new Request();
        this.contract = new Contract(sharkPKIComponent);
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
            this.protocolState = ProtocolState.Transferor.isActive();
        }
        this.protocolState = ProtocolState.Transferee.isActive();
    }

    @Override
    public Optional<MessageBuilder> sessionHandling(IMessage message, String sender) {
        this.sender = sender;
        checkDeviceState();
        switch (this.sessionState) {
            case NoSession:
                if (this.protocolState.equals(ProtocolState.Transferor.isActive())) {
                    this.messageObject = Optional.ofNullable(this.identification.transferor(message, sender).orElse(this.sessionState.resetState()));
                }
                // Only the NoSession combined with Transferee state creates an Advertisement message.
                this.messageObject = Optional.of((createAdvertisement()));
                this.noSession = true;
                this.sessionState = SessionState.NoSession.nextState();
                messageBuilder = Optional.of(new MessageBuilder(this.messageObject.get(), Channel.Advertisement.getChannel(), sender)).get();
                break;

            case Identification:
                if (!this.noSession) {
                    this.messageObject = Optional.empty();
                } else {
                    processIdentification(message);
                }
                if (this.messageObject.isPresent()) {
                    messageBuilder = Optional.of(new MessageBuilder(this.messageObject, Channel.Identification.getChannel(), sender)).get();
                }

                break;

            case Request:
                if (!this.noSession && !this.identification.getSessionComplete()) {
                    this.messageObject = Optional.empty();
                } else {
                    processRequest(message);
                }
                this.messageObject.ifPresent(object -> messageBuilder = new MessageBuilder(object, Channel.Request.getChannel(), sender));
                break;

            case Contract:
                if (!this.noSession && !this.identification.getSessionComplete() && !this.request.getSessionComplete()) {
                    this.messageObject = Optional.empty();
                } else {
                    processContract(message);
                }
                this.messageObject.ifPresent(object -> messageBuilder = new MessageBuilder(object, Channel.Contract.getChannel(), sender));
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
        if (this.protocolState.equals(ProtocolState.Transferor.isActive())) {
            this.messageObject = Optional.ofNullable(this.identification.transferor(message, sender).orElse(this.sessionState.resetState()));
        } else {
            this.messageObject = Optional.ofNullable(this.identification.transferee(message, sender).orElse(this.sessionState.resetState()));
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
        if (this.protocolState.equals(ProtocolState.Transferor.isActive())) {
            this.messageObject = Optional.ofNullable(this.request.transferor(message, sender).orElse(this.sessionState.resetState()));
        } else {
            this.messageObject = Optional.ofNullable(this.request.transferee(message, sender).orElse(this.sessionState.resetState()));
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
        if (protocolState.equals(ProtocolState.Transferor.isActive())) {
            this.messageObject = Optional.ofNullable(this.contract.transferor(message, sender).orElse(this.sessionState.resetState()));
        } else {
            this.messageObject = Optional.ofNullable(this.contract.transferee(message, sender).orElse(this.sessionState.resetState()));
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
        if (protocolState.equals(ProtocolState.Transferor)) {
            protocolState = ProtocolState.Transferee.isActive();
            this.deliveryContract.resetContractState();
        } else { // New transferor does not have to set the contract state. Already set in 'storeDeliveryContract' methode.
            protocolState = ProtocolState.Transferor.isActive();
        }
    }

    /**
     * Method to reset every list and current state, but not the ShippingLabel state!!!
     */
    public void resetAll() {
        this.noSession = true;
        this.identification.setSessionComplete(false);
        this.request.setSessionComplete(false);
        this.contract.setSessionComplete(false);
        this.identification.clearMessageList();
        this.request.clearMessageList();
        this.contract.clearMessageList();
        this.sessionState.resetState();
        this.deliveryContract.setContractSent(false);
    }
}
