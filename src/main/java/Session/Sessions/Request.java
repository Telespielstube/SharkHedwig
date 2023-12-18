package Session.Sessions;

import Message.Advertisement;
import Message.IMessage;
import Message.Identification.AbstractIdentification;
import Message.Identification.Response;
import Message.Request.AbstractRequest;
import Message.Request.Offer;
import Message.MessageFlag;
import Message.Request.OfferReply;

import java.util.Collections;
import java.util.Optional;
import java.util.TreeMap;

public class Request extends AbstractSession {

    private Offer offer;
    private OfferReply offerReply;

    public Request() {
        this.messageList = Collections.synchronizedSortedMap(new TreeMap<>());
    }


    @Override
    public Object unpackMessage(IMessage message) {
        Optional<AbstractRequest> messageObject = null;

        switch(message.getMessageFlag()) {
            case Offer:
                messageObject = Optional.of(handleOffer((Offer) message).get());
                break;
            case Ack:
                messageObject = Optional.of(handleResponse((Response) message).get()));
                break;
            default:
                break;
        }
        if (messageObject.isPresent()) {
            this.messageList.put(messageObject.get().getTimestamp(), messageObject);
        }
    }


    private Optional<OfferReply> handleOffer(Offer message) {

        return Optional.of(new OfferReply());
    }

}
