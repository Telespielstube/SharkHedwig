package Session;

import DeliveryContract.ShippingLabel;
import Message.IMessage;
import Message.Message;

import java.util.Optional;

public abstract class AbstractSession implements ISession {

    protected boolean sessionComplete = false;
    protected int timeOffset = 5000;

    public abstract Optional<Message> transferor(IMessage message, ShippingLabel shippingLabel, String sender);

    public abstract Optional<Message> transferee(IMessage message, String sender);

    public boolean getSessionComplete() {
        return this.sessionComplete;
    }

    public void setSessionComplete(boolean complete) {
        this.sessionComplete = complete;
    }
}
