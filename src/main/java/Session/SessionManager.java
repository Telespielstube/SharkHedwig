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
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

public class SessionManager implements Observer, ISessionManager {

    private SessionState sessionState;
    private ProtocolState protocolState;
    private AbstractSession identification;
    private AbstractSession request;
    private AbstractSession contract;
    private MessageBuilder messageBuilder;
    private String sender;
    private DeliveryContract deliveryContract;
    private Optional<Object> optionalMessage;
    private boolean contractCreated;
    private boolean labelCreated;
    private boolean noSession = false; // attribute because NoSession has no Session Object.
    private ShippingLabel shippingLabel;


    public SessionManager(SessionState sessionState, ProtocolState protocolState, ASAPPeer peer, SharkPKIComponent sharkPKIComponent) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.protocolState = protocolState;
        this.sessionState = sessionState;
        this.identification = new Identification(sharkPKIComponent);
        this.shippingLabel = shippingLabel;
        this.request = new Request();
        this.contract = new Contract(sharkPKIComponent);
        this.optionalMessage = Optional.empty();
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
    public void update(Observable o, Object arg) {
        if (o instanceof ShippingLabel ) {
            this.protocolState = ProtocolState.TRANSFEROR;
            this.labelCreated = ((ShippingLabel) o).getIsCreated();

        }
        if (o instanceof DeliveryContract) {
            this.contractCreated = ((DeliveryContract) o).getIsCreated();
            this.deliveryContract = ((DeliveryContract) o).get();
        }
    }

    @Override
    public Optional<MessageBuilder> sessionHandling(IMessage message, String sender) {
<<<<<<< HEAD
        this.sender = sender;
        checkDeviceState();
=======

>>>>>>> tmp
        switch (this.sessionState) {
            case NOSESSION:
                if (this.protocolState.equals(ProtocolState.TRANSFEROR) && this.labelCreated) {
                    this.optionalMessage = Optional.ofNullable(this.identification.transferor(message, sender).orElse(this.sessionState.resetState()));
                }
                // Only the NoSession combined with Transferee state creates an Advertisement message.
                this.optionalMessage = Optional.of((createAdvertisement()));
                this.noSession = true;
                this.sessionState = SessionState.NOSESSION.nextState();
                messageBuilder = Optional.of(new MessageBuilder(this.optionalMessage.get(), Channel.ADVERTISEMENT.getChannel(), sender)).get();
                break;

            case IDENTIFICATION:
                if (!this.noSession) {
                    this.optionalMessage = Optional.empty();
                } else {
                    processIdentification(message);
                }
                if (this.optionalMessage.isPresent()) {
                    messageBuilder = Optional.of(new MessageBuilder(this.optionalMessage, Channel.IDENTIFICATION.getChannel(), sender)).get();
                }
                break;

            case REQUEST:
                if (!this.noSession && !this.identification.getSessionComplete()) {
                    this.optionalMessage = Optional.empty();
                } else {
                    processRequest(message);
                }
                this.optionalMessage.ifPresent(object -> messageBuilder = new MessageBuilder(object, Channel.REQUEST.getChannel(), sender));
                break;

            case CONTRACT:
                if (!this.noSession && !this.identification.getSessionComplete() && !this.request.getSessionComplete()) {
                    this.optionalMessage = Optional.empty();
                } else {
                    processContract(message);
                }
                this.optionalMessage.ifPresent(object -> messageBuilder = new MessageBuilder(object, Channel.CONTRACT.getChannel(), sender));
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
        if (this.protocolState.equals(ProtocolState.TRANSFEROR)) {
            this.optionalMessage = Optional.ofNullable(this.identification.transferor(message, sender).orElse(this.sessionState.resetState()));
        } else {
            this.optionalMessage = Optional.ofNullable(this.identification.transferee(message, sender).orElse(this.sessionState.resetState()));
        }
        if (this.optionalMessage.isPresent() && this.identification.setSessionComplete(this.optionalMessage)) {
            this.identification.clearMessageList();
            this.sessionState = SessionState.IDENTIFICATION.nextState();
        }
        this.optionalMessage = Optional.empty();
    }

    /**
     * If the previous session is completed the received request message gets processed.
     *
     * @param message    Received request message
     */
    private void processRequest(IMessage message) {
        if (this.protocolState.equals(ProtocolState.TRANSFEROR)) {
            this.optionalMessage = Optional.ofNullable(this.request.transferor(message, sender).orElse(this.sessionState.resetState()));
        } else {
            this.optionalMessage = Optional.ofNullable(this.request.transferee(message, sender).orElse(this.sessionState.resetState()));
        }
        if (this.optionalMessage.isPresent() && this.request.setSessionComplete(this.optionalMessage)) {
            this.request.clearMessageList();
            this.sessionState = SessionState.REQUEST.nextState();
        }
        this.optionalMessage = Optional.empty();
    }

    /**
     * If the previous session is completed the received contract message gets processed.
     *
     * @param message    Received contract message
     */
    private void processContract(IMessage message) {
        if (protocolState.equals(ProtocolState.TRANSFEROR)) {
            this.optionalMessage = Optional.ofNullable(this.contract.transferor(message, sender).orElse(this.sessionState.resetState()));
        } else {
            this.optionalMessage = Optional.ofNullable(this.contract.transferee(message, sender).orElse(this.sessionState.resetState()));
        }
        if (this.optionalMessage.isPresent() && this.contract.setSessionComplete(this.optionalMessage)) {
            changeProtocolState();
            resetAll();
        }
        this.optionalMessage = Optional.empty();
    }

    /**
     * After the contract log is written it is assumed that the package is exchanged. Therefore, the states must switch
     * vice versa= and the ShippingLabel state must change as well.
     */
    public void changeProtocolState() {
        if (protocolState.equals(ProtocolState.TRANSFEROR)) {
            protocolState = ProtocolState.TRANSFEREE;
            this.deliveryContract.resetContractState();
            this.labelCreated = false;
        } else {
            protocolState = ProtocolState.TRANSFEROR;
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

    }
}
