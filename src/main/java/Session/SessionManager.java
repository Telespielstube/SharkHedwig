package Session;

import Battery.Battery;
import DeliveryContract.DeliveryContract;
import DeliveryContract.ShippingLabel;
import Location.GeoSpatial;
import ProtocolRole.ProtocolRole;
import Message.MessageBuilder;
import Message.Messageable;
import Message.Message;
import Message.MessageFlag;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import Session.State.ContractState;
import Session.State.NoSessionState;
import Session.State.RequestState;
import Session.State.SessionState;
import Setup.Channel;
import net.sharksystem.pki.SharkPKIComponent;

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

    public SessionManager(ShippingLabel shippingLabel, ProtocolRole protocolRole, DeliveryContract deliveryContract, Battery battery,
                          GeoSpatial geoSpatial, SharkPKIComponent sharkPKIComponent) {
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
        this.optionalMessage = getCurrentSessionState().handle(message, this.protocolRole, sender);
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

    private boolean checkStateStatus(MessageFlag messageFlag) {
        boolean complete = (messageFlag == MessageFlag.ADVERTISEMENT || messageFlag == MessageFlag.SOLICITATION ||
                messageFlag == MessageFlag.OFFER_REPLY || messageFlag == MessageFlag.CONFIRM || messageFlag == MessageFlag.RELEASE ||
                messageFlag == MessageFlag.COMPLETE)
                ? true
                : false;
        return complete;
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
