package Session;

import DeliveryContract.DeliveryContract;
import DeliveryContract.ShippingLabel;
import Misc.Utilities;
import Setup.ProtocolState;
import Message.*;
import net.sharksystem.pki.SharkPKIComponent;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import Setup.Channel;

public class SessionManager implements Observer, ISessionManager {

    private SessionState sessionState;
    private ProtocolState protocolState;
    private AbstractSession authentification;
    private AbstractSession request;
    private AbstractSession contract;
    private MessageBuilder messageBuilder;
    private DeliveryContract deliveryContract;
    private ShippingLabel shippingLabel;
    private Optional<Object> optionalMessage;
    private boolean contractCreated;
    private boolean labelCreated;
    private boolean noSession = false; // attribute because NoSession has no Session Object.
    private String sender;

    public SessionManager(SessionState sessionState, ProtocolState protocolState, SharkPKIComponent sharkPKIComponent) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.protocolState = protocolState;
        this.sessionState = sessionState;
        this.authentification = new Authentication(sharkPKIComponent);
        this.contract = new Contract(sharkPKIComponent);
        this.request = new Request((Contract) this.contract);
        this.shippingLabel = null;
        this.optionalMessage = Optional.empty();
        this.contractCreated = false;
        this.sender = "";
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof ShippingLabel ) {
            this.protocolState = ProtocolState.TRANSFEROR;
            this.labelCreated = ((ShippingLabel) o).getIsCreated();
            this.shippingLabel = ((ShippingLabel) o).get();
        }
        if (o instanceof DeliveryContract) {
            this.contractCreated = ((DeliveryContract) o).getIsCreated();
            this.deliveryContract = ((DeliveryContract) o).get();
        }
    }

    @Override
    public Optional<MessageBuilder> sessionHandling(IMessage message, String sender) {
        this.sender = sender;
        switch (this.sessionState) {
            case NO_SESSION:
                if (this.protocolState.equals(ProtocolState.TRANSFEROR) && this.labelCreated) {
                    this.optionalMessage = Optional.ofNullable(this.authentification.transferor(message,
                            this.sender).orElse(this.sessionState.resetState()));
                }
                // Only the NoSession combined with Transferee state creates an Advertisement message.
                this.noSession = true;
                this.optionalMessage = Optional.of(new MessageBuilder(createAdvertisement(),
                        Channel.ADVERTISEMENT.getChannel(), this.sender));
                this.optionalMessage.ifPresent(object -> {
                    this.sessionState = SessionState.NO_SESSION.nextState();
                    this.noSession = true;
                });
                break;

            case AUTHENTIFICATION:
                if (!this.noSession) {
                    this.optionalMessage = Optional.empty();
                } else {
                    processAuthentification(message);
                }
                this.optionalMessage.ifPresent(object -> this.messageBuilder = new MessageBuilder(this.optionalMessage, Channel.AUTHENTIFICATION.getChannel(), this.sender));
                break;

            case REQUEST:
                if (!this.noSession && !this.authentification.getSessionComplete()) {
                    this.optionalMessage = Optional.empty();
                } else {
                    processRequest(message);
                }
                this.optionalMessage.ifPresent(object -> this.messageBuilder = new MessageBuilder(object, Channel.REQUEST.getChannel(), this.sender));
                break;

            case CONTRACT:
                if (!this.noSession && this.authentification.getSessionComplete() && this.request.getSessionComplete()) {
                    this.optionalMessage = Optional.empty();
                } else {
                    processContract(message);
                }
                this.optionalMessage.ifPresent(object -> this.messageBuilder = new MessageBuilder(object, Channel.CONTRACT.getChannel(), this.sender));
                break;

            default:
                System.err.println("There was no session flag: " + this.sessionState);
                resetAll();
                break;
        }

        return Optional.ofNullable(this.messageBuilder);

    }

    /**
     * A small message just to advertise a delivery service. Does not belong to a session.
     *
     * @return    Advertisement message object.
     */
    private Advertisement createAdvertisement() {
        return new Advertisement(Utilities.createUUID(), MessageFlag.ADVERTISEMENT, Utilities.createTimestamp(), true);
    }

    /**
     * If the previous session is completed the received identification message gets processed.
     *
     * @param message    Received identification message
     */
    private void processAuthentification(IMessage message) {
        this.optionalMessage = this.protocolState.equals(ProtocolState.TRANSFEROR)
                ? Optional.ofNullable(this.authentification.transferor(message, this.sender).orElse(this.sessionState.resetState()))
                : Optional.ofNullable(this.authentification.transferee(message, this.sender).orElse(this.sessionState.resetState()));
        if (this.optionalMessage.isPresent() && this.authentification.getSessionComplete()) {
            this.authentification.clearMessageList();
            this.sessionState = SessionState.AUTHENTIFICATION.nextState();
        }
        this.optionalMessage = Optional.empty();
    }

    /**
     * If the previous session is completed the received request message gets processed.
     *
     * @param message    Received request message
     */
    private void processRequest(IMessage message) {
        this.optionalMessage = this.protocolState.equals(ProtocolState.TRANSFEROR)
                ? Optional.ofNullable(this.request.transferor(message, this.sender).orElse(this.sessionState.resetState()))
                : Optional.ofNullable(this.request.transferee(message, this.sender).orElse(this.sessionState.resetState()));
        if (this.optionalMessage.isPresent() && this.request.getSessionComplete()) {
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
        this.optionalMessage = protocolState.equals(ProtocolState.TRANSFEROR) ?
                Optional.ofNullable(this.contract.transferor(message, this.sender).orElse(this.sessionState.resetState())) :
                Optional.ofNullable(this.contract.transferee(message, this.sender).orElse(this.sessionState.resetState()));
        if (this.optionalMessage.isPresent() && this.contract.getSessionComplete()) {
            changeProtocolState();
            resetAll();
        }
        this.optionalMessage = Optional.empty();
    }

    /**
     * After the contract log is written it is assumed that the package is exchanged. Therefore, the states must switch
     * vice versa= and the ShippingLabel state must change as well.
     */
    private void changeProtocolState() {
        if (protocolState.equals(ProtocolState.TRANSFEROR)) {
            protocolState = ProtocolState.TRANSFEREE;
            this.deliveryContract.resetContractState();
            this.labelCreated = false;
        } else {
            protocolState = ProtocolState.TRANSFEROR;
            this.contractCreated = true;
        }
    }

    /**
     * Method to reset every list and current state, but not the ShippingLabel state!!!
     */
    private void resetAll() {
        this.noSession = true;
        this.authentification.getSessionComplete(false);
        this.request.getSessionComplete(false);
        this.contract.getSessionComplete(false);
        this.authentification.clearMessageList();
        this.request.clearMessageList();
        this.contract.clearMessageList();
        this.sessionState.resetState();
    }
}
