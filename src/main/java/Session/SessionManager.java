package Session;

import DeliveryContract.DeliveryContract;
import DeliveryContract.ShippingLabel;
import Message.Solicitation;
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
    private final ReceivedMessageList receivedMessageList;
    private final AbstractSession request;
    private final AbstractSession contract;
    private MessageBuilder messageBuilder;
    private DeliveryContract deliveryContract;
    private ShippingLabel shippingLabel;
    private Optional<Message> optionalMessage;
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
                processNoSession();;
                break;
            case REQUEST:
                if (this.noSession) {
                    processRequest(message);
                    this.optionalMessage.ifPresent(object
                            -> this.messageBuilder = new MessageBuilder(object, Channel.REQUEST.getChannel(), this.sender));
                }
                break;
            case CONTRACT:
                if (this.noSession && this.request.getSessionComplete()) {
                    processContract(message);
                    this.optionalMessage.ifPresent(object
                            -> this.messageBuilder = new MessageBuilder(object, Channel.CONTRACT.getChannel(), this.sender));
                }
                break;
            default:
                System.err.println("There was no session flag: " + this.sessionState);
                resetAll();
                break;
        }
        return Optional.ofNullable(this.messageBuilder);

    }

    /**
     * This represents the default state of every drone. It handles the creation of advertisment and solicitation
     * messages.
     */
    private void processNoSession() {
        if (this.protocolState.equals(ProtocolState.TRANSFEROR) && this.shippingLabelCreated) {
            this.optionalMessage = Optional.of(new Solicitation(Utilities.createUUID(), MessageFlag.SOLICITATION, Utilities.createTimestamp(), true));
        } else {
            this.optionalMessage = Optional.of(new Advertisement(Utilities.createUUID(), MessageFlag.ADVERTISEMENT, Utilities.createTimestamp(), true));
        }
        this.optionalMessage.ifPresent(object -> {
            this.messageBuilder = new MessageBuilder(object, Channel.NO_SESSION.getChannel(), this.sender);
            this.receivedMessageList.addMessageToList(object);
            this.sessionState = SessionState.NO_SESSION.nextState();
            this.noSession = true;
        });
    }
    /**
     * If the previous session is completed the received request message gets processed.
     *
     * @param message    Received request message
     */
    private void processRequest(IMessage message) {
        this.optionalMessage = this.protocolState.equals(ProtocolState.TRANSFEROR)
                ? this.request.transferor(message, this.sender)
                : this.request.transferee(message, this.sender);
        this.optionalMessage.ifPresent(object -> {
            if (this.request.getSessionComplete()) {
                this.sessionState = SessionState.REQUEST.nextState();
            }
        });
//        if (this.optionalMessage.isPresent() && this.request.getSessionComplete()) {
//            this.sessionState = SessionState.REQUEST.nextState();
//        } else {
//            this.sessionState.resetState();
//        }
    }

    /**
     * If the previous session is completed the received contract message gets processed.
     *
     * @param message    Received contract message
     */
    private void processContract(IMessage message) {
        this.optionalMessage = protocolState.equals(ProtocolState.TRANSFEROR)
                ? Optional.of(this.contract.transferor(message, this.sender).get())
                : Optional.of(this.contract.transferee(message, this.sender).get());
        if (this.optionalMessage.isPresent() && this.contract.getSessionComplete()) {
            changeProtocolState();
            resetAll();
        } else {
            this.sessionState.resetState();
        }
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
     * Method to reset everything to default!!! Resets the session state to no session, clears the received message list
     * and sets the session to incomplete.
     */
    private void resetAll() {
        this.noSession = true;
        this.request.setSessionComplete(false);
        this.contract.setSessionComplete(false);
        this.receivedMessageList.clearMessageList();
        this.sessionState.resetState();
    }
}
