package Session;

import Battery.Battery;
import DeliveryContract.DeliveryContract;
import DeliveryContract.ShippingLabel;
import Location.Locationable;
import Setup.ProtocolRole;
import Setup.State.ProtocolState;
import Message.*;
import net.sharksystem.pki.SharkPKIComponent;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import Setup.Channel;

public class SessionManager implements Observer, ISessionManager {

    private ProtocolRole protocolRole;
    private Session session;
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
    private String sender;
    private Battery battery;

    public SessionManager(Session session, ProtocolRole protocolRole, ReceivedMessageList receivedMessageList, Battery battery, Locationable geoSpatial, SharkPKIComponent sharkPKIComponent) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.session = session;
        this.protocolRole = protocolRole;
        this.receivedMessageList = receivedMessageList;
        this.contract = new Contract(sharkPKIComponent, this.receivedMessageList);
        this.request = new Request((Contract) this.contract, battery, geoSpatial, this.receivedMessageList);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof ShippingLabel ) {
            this.protocolRole.getTransferorState().isActive();
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
        this.optionalMessage.ifPresent(object
                -> this.messageBuilder = new MessageBuilder(object, Channel.REQUEST.getChannel(), this.sender));
        return Optional.ofNullable(this.messageBuilder);
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
        this.request.setSessionComplete(false);
        this.contract.setSessionComplete(false);
        this.receivedMessageList.clearMessageList();
        this.session.getCurrentState().resetState();
    }
}
