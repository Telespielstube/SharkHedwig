package Session;

import Battery.Battery;
import DeliveryContract.DeliveryContract;
import DeliveryContract.ShippingLabel;
import Location.Locationable;
import ProtocolRole.ProtocolRole;
import ProtocolRole.State.ProtocolState;
import Message.*;
import net.sharksystem.pki.SharkPKIComponent;

import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import Setup.Channel;

public class SessionManager implements Observer, ISessionManager {

    private ProtocolRole protocolRole;
    private Session session;
    private ProtocolState protocolState;
    private final AbstractSession request;
    private final AbstractSession contract;
    private MessageBuilder messageBuilder;
    private DeliveryContract deliveryContract;
    private ShippingLabel shippingLabel;
    private Optional<Message> optionalMessage;
    private boolean deliveryContractCreated;
    private boolean shippingLabelCreated;
    private String sender;
    private Battery battery;

    public SessionManager(Session session, ProtocolRole protocolRole, Battery battery, Locationable geoSpatial, SharkPKIComponent sharkPKIComponent) {
        this.session = session;
        this.protocolRole = protocolRole;
        this.contract = new Contract(sharkPKIComponent);
        this.request = new Request((Contract) this.contract, battery, geoSpatial);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof ShippingLabel ) {
            this.protocolRole.getTransferorState().changeRole();
            this.shippingLabelCreated = ((ShippingLabel) o).getIsCreated();
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
            this.messageBuilder = new MessageBuilder(this.optionalMessage, Channel.REQUEST.getChannel(), this.sender);
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
//        if (protocolState.equals(ProtocolState.TRANSFEROR)) {
//            protocolState = ProtocolState.TRANSFEREE;
//            this.deliveryContract.resetContractState();
//            this.shippingLabelCreated = false;
//        } else {
//            protocolState = ProtocolState.TRANSFEROR;
//            this.deliveryContractCreated = true;
//        }
//    }

}
