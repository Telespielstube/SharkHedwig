package Session.Sessions;

import Message.IMessage;
import Message.AckMessage;
import Message.Request.*;
import Message.MessageFlag;
import Message.Request.OfferReply;
import Misc.Utilities;

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
    public Optional<Object> transferor(IMessage message) {
        Optional<AbstractRequest> messageObject = null;
        switch(message.getMessageFlag()) {
            case Offer:
                messageObject = Optional.of(handleOffer((Offer) message).get());
                break;
            case Confirm:
                messageObject = Optional.ofNullable(handleConfirm((Confirm) message).orElse(null));
            default:
                System.err.println("Missing message flag.");
                break;
        }
        if (messageObject.isPresent()) {
            this.messageList.put(messageObject.get().getTimestamp(), messageObject);
        } else {
            this.messageList.clear();
        }
        return Optional.ofNullable(messageObject);
    }

    @Override
    public Optional<Object> transferee(IMessage message) {
        Optional<AbstractRequest> messageObject = null;

        switch(message.getMessageFlag()) {
            case OfferReply:
                messageObject = Optional.ofNullable(handleOfferReply((Offer) message).orElse(null));
                break;
            case Ack:
                if (handleAckMessage((AckMessage) message)) {
                    messageObject.orElse(null);
                }
                break;
            default:
                System.err.println("Missing message flag.");
                break;
        }
        if (messageObject.isPresent()) {
            this.messageList.put(messageObject.get().getTimestamp(), messageObject);
        }
        return Optional.ofNullable(messageObject);
    }


    /**
     * Processes the data
     * @param message
     * @return
     */
    private Optional<OfferReply> handleOffer(Offer message) {

       // return Optional.of(new OfferReply(this.offerReply.createUUID(), MessageFlag.OfferReply, Utilities.createTimestamp(),  ));
        return Optional.empty();
    }

    /**
     *
     * @param message
     * @return
     */
    private Optional<Confirm> handleOfferReply(Offer message) {
        // The recipient of this message has to do the same data processing
        return Optional.empty();
    }

    /**
     *
     * @param message
     * @return
     */
    private Optional<Confirm> handleConfirm(Confirm message) {

        return Optional.of(new Confirm());
    }


}
