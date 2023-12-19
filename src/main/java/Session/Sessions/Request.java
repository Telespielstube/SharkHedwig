package Session.Sessions;

import DeliveryContract.DeliveryContract;
import DeliveryContract.ShippingLabel;
import Message.IMessage;
import Message.MessageFlag;
import Message.Request.*;
import Battery.Battery;
import Location.GeoCalculation;
import Message.Request.OfferReply;
import Misc.LogEntry;
import Misc.SessionLogger;
import Misc.Utilities;
import Setup.Constant;

import java.util.Collections;
import java.util.Optional;
import java.util.TreeMap;

public class Request extends AbstractSession {

    private Offer offer;
    private OfferReply offerReply;
    private Confirm confirm;
    private Battery battery;
    private GeoCalculation geoCalculation;
    private DeliveryContract deliveryContract;

    public Request() {
        this.battery = new Battery();
        this.geoCalculation = new GeoCalculation();
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
                SessionLogger.writeEntry(new LogEntry(), Constant.RequestLogPath.getAppConstant());
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
                messageObject = Optional.ofNullable(handleOfferReply((OfferReply) message).orElse(null));
                break;
            case Confirm:
                messageObject = Optional.ofNullable(handleConfirm((Confirm) message).orElse(null));
                SessionLogger.writeEntry(new LogEntry(), Constant.RequestLogPath.getAppConstant());
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
       ShippingLabel shippingData = this.deliveryContract.getShippingLabel();
       if (processOfferData(shippingData, message)) {
           return Optional.of(new OfferReply(this.offerReply.createUUID(), MessageFlag.OfferReply, Utilities.createTimestamp(), shippingData.getPackageWeight(), shippingData.getPackageDestination()));
       }
       return Optional.empty();
    }


    /**
     * The recipient of the OfferRepy message has to process the received data sa well. Just to double
     * check all received data are verified with the local data set. This needs to be done!!!
     *
     * @param message    OfferReply message object
     * @return           Optional.empty if the calculation did not get verified, or Corfim message if data got verified.
     */
    private Optional<Confirm> handleOfferReply(OfferReply message) {

        if (compareTimestamp(message.getTimestamp())) {
            return Optional.of(new Confirm(this.confirm.createUUID(), MessageFlag.Confirm, Utilities.createTimestamp(), true));
        }
        return Optional.empty();
    }

    /**
     * Validates the received Confirm message object and if no Confirm object is already saved
     * a new Confirm object gets created.
     *
     * @param message    Confirm messge object.
     * @return           An empty optional if a Confirm object is found.
     */
    private Optional<Confirm> handleConfirm(Confirm message) {
        Object object = getLastValueFromList();
        if (compareTimestamp(message.getTimestamp()) && message.getIsConfirmed() && (!(object instanceof Confirm))) {
            return Optional.of(new Confirm(this.confirm.createUUID(), MessageFlag.Confirm, Utilities.createTimestamp(), true));
        }
        return Optional.empty();
    }

    /**
     * This methods processes the received offer data and calculates from the crucial data set if the
     * delivery service is possible. This need to be done!!!
     *
     * @return    True if all data is valid and package can be delivered to destination.
     */
    private boolean processOfferData(ShippingLabel shippingLabel, Offer message) {
        return true;
    }
}
