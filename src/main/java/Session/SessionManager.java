package Session;

import Battery.Battery;
import DeliveryContract.DeliveryContract;
import DeliveryContract.ShippingLabel;
import Location.Locationable;
import ProtocolRole.ProtocolRole;
import Message.*;
import net.sharksystem.pki.SharkPKIComponent;

import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import Setup.Channel;

public class SessionManager implements Observer, ISessionManager {

    private ProtocolRole protocolRole;
    private Session session;
    private final AbstractSession request;
    private final AbstractSession contract;
    private MessageBuilder messageBuilder;
    private DeliveryContract deliveryContract;
    private ShippingLabel shippingLabel;
    private Optional<Message> optionalMessage;
    private boolean deliveryContractCreated;

    public SessionManager(Session session, ProtocolRole protocolRole, Battery battery, Locationable geoSpatial, SharkPKIComponent sharkPKIComponent) {
        this.session = session;
        this.protocolRole = protocolRole;
        this.contract = new Contract(sharkPKIComponent);
        this.request = new Request((Contract) this.contract, battery, geoSpatial);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof ShippingLabel ) {
            this.protocolRole.getTranfereeState().changeRole();
            this.shippingLabel = ((ShippingLabel) o).get();
        }
        if (o instanceof DeliveryContract) {
            this.deliveryContractCreated = ((DeliveryContract) o).getIsCreated();
            this.deliveryContract = ((DeliveryContract) o).get();
        }
    }

    @Override
    public Optional<MessageBuilder> sessionHandling(Messageable message, String sender) {
        this.optionalMessage = this.session.getCurrentState().handle(message, sender);
        if (this.optionalMessage.isPresent()) {
            if (this.session.getCurrentState().equals(this.session.getRequestState())) {
                this.messageBuilder = new MessageBuilder(this.optionalMessage.get(), Channel.REQUEST.getChannel(), sender);
            } else if (this.session.getCurrentState().equals(this.session.getContractState())) {
                this.messageBuilder = new MessageBuilder(this.optionalMessage.get(), Channel.CONTRACT.getChannel(), sender);
            }
            MessageList.addMessageToList(optionalMessage.get());
        } else {
            MessageList.clearMessageList();
            this.session.getCurrentState().resetState();
        }
        return Optional.ofNullable(this.messageBuilder);
    }

//    /**
//     * After the contract log is written it is assumed that the package is exchanged. Therefore, the states must switch
//     * vice versa= and the ShippingLabel state must change as well.
//     */
//    private void changeProtocolState() {
//        if (protocolRole.getCurrentState().equals(protocolRole.getTransferorState())) {
//            p
//            this.deliveryContract.resetContractState();
//            this.shippingLabelCreated = false;
//        } else {
//            protocolState = ProtocolState.TRANSFEROR;
//            this.deliveryContractCreated = true;
//        }
//    }

}
