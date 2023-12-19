package Session.Sessions;

import Message.Advertisement;
import Message.IMessage;
import Message.Identification.AbstractIdentification;
import Message.Identification.Response;
import Message.Request.AbstractRequest;
import Message.Request.Confirm;
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


//    @Override
//    public Optional<Object> unpackMessage(IMessage message) {
//        Optional<AbstractRequest> messageObject = null;
//
//        switch(message.getMessageFlag()) {
//            case Offer:
//                messageObject = Optional.of(handleOffer((Offer) message).get());
//                break;
//            case Confirm:
//                messageObject = Optional.of(handleConfirm((Confirm) message).get());
//                break;
//            default:
//                break;
//        }
//        if (messageObject.isPresent()) {
//            this.messageList.put(messageObject.get().getTimestamp(), messageObject);
//        }
//    }


    private Optional<OfferReply> handleOffer(Offer message) {

        return Optional.of(new OfferReply());
    }

    private Optional<Confirm> handleConfirm(Confirm message) {
        return Optional.of(new Confirm());
    }

    @Override
    public Optional<Object> transferor(IMessage message) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> transferee(IMessage message) {
        return Optional.empty();
    }
}
