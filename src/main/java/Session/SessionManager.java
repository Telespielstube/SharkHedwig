package Session;
import DeliveryContract.DeliveryContract;
import DeliveryContract.ShippingLabel;
import ProtocolRole.ProtocolRole;
import Message.MessageBuilder;
import Message.Messageable;
import Message.Message;
import Message.MessageFlag;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import Session.State.*;
import Setup.Channel;

public class SessionManager implements Observer, ISessionManager {
    private ProtocolRole protocolRole;
    private MessageBuilder messageBuilder;
    private DeliveryContract deliveryContract;
    private ShippingLabel shippingLabel;
    private Optional<Message> optionalMessage;
    private SessionState currentSessionState;
    private final SessionState noSessionState;
    private final SessionState requestState;
    private final SessionState contractState;

    public SessionManager(ShippingLabel shippingLabel, ProtocolRole protocolRole, DeliveryContract deliveryContract) {
        this.shippingLabel = shippingLabel;
        this.protocolRole = protocolRole;
        this.deliveryContract = deliveryContract;
        this.noSessionState = new NoSessionState(this);
        this.requestState = new RequestState(this);
        this.contractState = new ContractState(this);
        this.currentSessionState = this.noSessionState;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof ShippingLabel ) {
            this.protocolRole.setProtocolState(this.protocolRole.getTransferorState());
            this.shippingLabel = ((ShippingLabel) o).get();
        }
        if (o instanceof DeliveryContract) {
            this.deliveryContract = ((DeliveryContract) o).get();
        }
    }

    @Override
    public Optional<MessageBuilder> sessionHandling(Messageable message, String sender) {
        this.optionalMessage = getCurrentSessionState().handle(message, this.protocolRole, this.shippingLabel, this.deliveryContract, sender);
        if (this.optionalMessage.isPresent()) {
            if (getCurrentSessionState().equals(getNoSessionState())) {
                this.messageBuilder = new MessageBuilder(this.optionalMessage.get(), Channel.NO_SESSION.getChannel(), sender);
            }
            if (getCurrentSessionState().equals(getRequestState())) {
                this.messageBuilder = new MessageBuilder(this.optionalMessage.get(), Channel.REQUEST.getChannel(), sender);
            }
            if (getCurrentSessionState().equals(getContractState())) {
                this.messageBuilder = new MessageBuilder(this.optionalMessage.get(), Channel.CONTRACT.getChannel(), sender);
            }
            if (checkStateStatus(this.optionalMessage.get().getMessageFlag())) {
                this.currentSessionState.nextState();
            }
        } else {
            getCurrentSessionState().resetState();
        }
        return Optional.ofNullable(this.messageBuilder);
    }

    /**
     * The seeion progress is checked using the message flag.
     *
     * @param messageFlag    Unique message identifier.
     *
     * @return               boolean value true if the condition is true or false if not.
     */
    private boolean checkStateStatus(MessageFlag messageFlag) {
        if (protocolRole.getCurrentProtocolState().equals(protocolRole.getTransferorState())
                && messageFlag == MessageFlag.ADVERTISEMENT
                || messageFlag == MessageFlag.CONFIRM
                || messageFlag == MessageFlag.COMPLETE) {
            return true;
        } else if (protocolRole.getCurrentProtocolState().equals(protocolRole.getTransfereeState())
                && messageFlag == MessageFlag.SOLICITATION
                || messageFlag == MessageFlag.OFFER_REPLY
                || messageFlag == MessageFlag.RELEASE) {
            return true;
        }
        return false;
    }

    /**
     * The following methods are getters and setters to control the session states.
     */
    public SessionState getCurrentSessionState() {
        return this.currentSessionState;
    }
    public void setSessionState(SessionState sessionState) {
        this.currentSessionState = sessionState;
    }

    public SessionState getNoSessionState() {
        return this.noSessionState;
    }

    public SessionState getRequestState() {
        return this.requestState;
    }

    public SessionState getContractState() {
        return this.contractState;
    }
}
