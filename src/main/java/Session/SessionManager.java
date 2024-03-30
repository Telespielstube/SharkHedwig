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
    private ReceivedMessageList receivedMessageList;
    private AbstractSession request;
    private AbstractSession contract;
    private MessageBuilder messageBuilder;
    private DeliveryContract deliveryContract;
    private ShippingLabel shippingLabel;
    private Optional<Object> optionalMessage;
    private boolean deliveryContractCreated;
    private boolean shippingLabelCreated;
    private boolean noSession = false; // attribute because NoSession has no Session Object.
    private String sender;

    public SessionManager(SessionState sessionState, ProtocolState protocolState, ReceivedMessageList receivedMessageList, SharkPKIComponent sharkPKIComponent) throws NoSuchPaddingException, NoSuchAlgorithmException {

        this.sessionState = sessionState;
        this.protocolState = protocolState;
        this.receivedMessageList = receivedMessageList;
        this.contract = new Contract(sharkPKIComponent, this.receivedMessageList);
        this.request = new Request((Contract) this.contract, this.receivedMessageList);
        this.shippingLabel = null;
        this.optionalMessage = Optional.empty();
        this.deliveryContractCreated = false;
        this.sender = "";
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof ShippingLabel ) {
            this.protocolState = ProtocolState.TRANSFEROR;
            this.shippingLabelCreated = ((ShippingLabel) o).getIsCreated();
            this.shippingLabel = ((ShippingLabel) o).get();
        }
        if (o instanceof DeliveryContract) {
            this.deliveryContractCreated = ((DeliveryContract) o).getIsCreated();
            this.deliveryContract = ((DeliveryContract) o).get();
        }
    }

    @Override
    public Optional<MessageBuilder> sessionHandling(IMessage message, String sender) {
        this.sender = sender;
        switch (this.sessionState) {
            case NO_SESSION:
                this.optionalMessage = this.protocolState.equals(ProtocolState.TRANSFEROR) && this.shippingLabelCreated
                        ? Optional.ofNullable(this.request.transferor(message, this.sender).orElse(this.sessionState.resetState()))
                        : Optional.of(new MessageBuilder(createAdvertisement(), Channel.ADVERTISEMENT.getChannel(), this.sender));
                this.optionalMessage.ifPresent(object -> {
                    this.sessionState = SessionState.NO_SESSION.nextState();
                    this.noSession = true;
                });
                break;

            case REQUEST:
                this.optionalMessage = !this.noSession ? Optional.empty() : processRequest(message);
                this.optionalMessage.ifPresent(object -> this.messageBuilder = new MessageBuilder(object, Channel.REQUEST.getChannel(), this.sender));
                break;

            case CONTRACT:
                this.optionalMessage = !this.noSession && this.request.getSessionComplete() ? Optional.empty() : processContract(message);
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
     * If the previous session is completed the received request message gets processed.
     *
     * @param message    Received request message
     */
    private Optional<Object> processRequest(IMessage message) {
        this.optionalMessage = this.protocolState.equals(ProtocolState.TRANSFEROR)
                ? Optional.ofNullable(this.request.transferor(message, this.sender).orElse(this.sessionState.resetState()))
                : Optional.ofNullable(this.request.transferee(message, this.sender).orElse(this.sessionState.resetState()));
        if (this.optionalMessage.isPresent() && this.request.getSessionComplete()) {
         //   this.request.clearMessageList();
            this.sessionState = SessionState.REQUEST.nextState();
        }
        return Optional.empty();
    }

    /**
     * If the previous session is completed the received contract message gets processed.
     *
     * @param message    Received contract message
     */
    private Optional<Object> processContract(IMessage message) {
        this.optionalMessage = protocolState.equals(ProtocolState.TRANSFEROR) ?
                Optional.ofNullable(this.contract.transferor(message, this.sender).orElse(this.sessionState.resetState())) :
                Optional.ofNullable(this.contract.transferee(message, this.sender).orElse(this.sessionState.resetState()));
        if (this.optionalMessage.isPresent() && this.contract.getSessionComplete()) {
            changeProtocolState();
            resetAll();
        }
        return Optional.empty();
    }

    /**
     * After the contract log is written it is assumed that the package is exchanged. Therefore, the states must switch
     * vice versa= and the ShippingLabel state must change as well.
     */
    private void changeProtocolState() {
        if (protocolState.equals(ProtocolState.TRANSFEROR)) {
            protocolState = ProtocolState.TRANSFEREE;
            this.deliveryContract.resetContractState();
            this.shippingLabelCreated = false;
        } else {
            protocolState = ProtocolState.TRANSFEROR;
            this.deliveryContractCreated = true;
        }
    }

    /**
     * Method to reset every list and current state, but not the ShippingLabel state!!!
     */
    private void resetAll() {
        this.noSession = true;
        this.request.getSessionComplete(false);
        this.contract.getSessionComplete(false);
//        this.request.clearMessageList();
//        this.contract.clearMessageList();
        this.sessionState.resetState();
    }
}
