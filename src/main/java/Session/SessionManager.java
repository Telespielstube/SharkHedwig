package Session;

import DeliveryContract.DeliveryContract;
import DeliveryContract.ShippingLabel;
import ProtocolRole.ProtocolRole;
import Message.MessageBuilder;
import Message.MessageList;
import Message.Messageable;
import Message.Message;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import Setup.Channel;

public class SessionManager implements Observer, ISessionManager {

    private ProtocolRole protocolRole;
    private Session session;
    private MessageBuilder messageBuilder;
    private DeliveryContract deliveryContract;
    private ShippingLabel shippingLabel;
    private Optional<Message> optionalMessage;

    public SessionManager(Session session, ProtocolRole protocolRole, ShippingLabel shippingLabel, DeliveryContract deliveryContract) {
        this.session = session;
        this.protocolRole = protocolRole;
        this.shippingLabel = shippingLabel;
        this.deliveryContract = deliveryContract;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof ShippingLabel ) {
            this.protocolRole.setProtocolState(this.protocolRole.getTransfereeState());
            this.shippingLabel = ((ShippingLabel) o).get();
        }
        if (o instanceof DeliveryContract) {
            this.deliveryContract = ((DeliveryContract) o).get();
        }
    }

    @Override
    public Optional<MessageBuilder> sessionHandling(Messageable message, String sender) {
        this.optionalMessage = this.session.getCurrentSessionState().handle(message, this.protocolRole,
                this.shippingLabel, this.deliveryContract, sender);
        if (this.optionalMessage.isPresent()) {
            if (this.session.getCurrentSessionState().equals(this.session.getNoSessionState())) {
                this.messageBuilder = new MessageBuilder(this.optionalMessage.get(), Channel.REQUEST.getChannel(), sender);
            }
            if (this.session.getCurrentSessionState().equals(this.session.getRequestState())) {
                this.messageBuilder = new MessageBuilder(this.optionalMessage.get(), Channel.REQUEST.getChannel(), sender);
            }
            if (this.session.getCurrentSessionState().equals(this.session.getContractState())) {
                this.messageBuilder = new MessageBuilder(this.optionalMessage.get(), Channel.CONTRACT.getChannel(), sender);
            }
            MessageList.addMessageToList(optionalMessage.get());
        } else {
            MessageList.clearMessageList();
            this.session.getCurrentSessionState().resetState();
        }
        return Optional.ofNullable(this.messageBuilder);
    }
}
