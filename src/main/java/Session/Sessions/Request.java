package Session.Sessions;

import Message.IMessage;
import Message.Request.Offer;
import Message.MessageFlag;
import java.util.Collections;
import java.util.TreeMap;

public class Request extends AbstractSession {


    public Request() {
        this.messageList = Collections.synchronizedSortedMap(new TreeMap<>());
    }


    @Override
    public Object unpackMessage(IMessage message) {
        if (message.getMessageFlag().equals(MessageFlag.Offer)) {
            return handleOffer((Offer) message);
        } else if (message.getMessageFlag().equals(MessageFlag.OfferReply)) {
            
        }
        return null;
    }


    public Offer handleOffer(Offer message) {
        return null;
    }

}
