package Session;

import DeliveryContract.IContractComponent;
import Misc.LogEntry;
import Misc.Utilities;
import Setup.Channel;
import Setup.DeviceState;
import Message.*;
import Session.Sessions.*;
import net.sharksystem.asap.ASAPPeer;
import net.sharksystem.pki.SharkPKIComponent;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class SessionManager implements ISessionManager {

    private SharkPKIComponent sharkPKIComponent;
    private ASAPPeer peer;
    private SessionState sessionState;
    private DeviceState isTransferor;
    private Identification identification;
    private Request request;
    private Contract contract;
    private IMessageHandler messageHandler;
    private String sender;
    private Advertisement advertisement;
    private LogEntry logEntry;
    private MessageBuilder messageBuilder;
    private IContractComponent shippingLabel;

    public SessionManager() {}

    public SessionManager(MessageHandler messageHandler, SessionState sessionState, DeviceState isTransferor, ASAPPeer peer, SharkPKIComponent sharkPKIComponent) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.messageHandler = messageHandler;
        this.peer = peer;
        this.sharkPKIComponent = sharkPKIComponent;
        this.isTransferor = isTransferor;
        this.sessionState = sessionState;
        this.identification = new Identification(this.sharkPKIComponent);
        this.request = new Request();
        this.contract = new Contract(this.messageHandler, this.sharkPKIComponent);
    }

    /**
     * A small message just to advertise a delivery service. Does not belong to a session.
     *
     * @return    Advertisement message object.
     */
    private Advertisement createAdvertisement() {
        return new Advertisement(null, true, MessageFlag.Advertisement, Utilities.createTimestamp());
    }

    @Override
    public boolean checkTransferorState() {
        if (shippingLabel.isCreated()) {
            DeviceState.Transferor.isActive();
            return true;
        }
        return false;
    }

    /**
     * Method that handles all the sessions.
     *
     * @param message    Incomming generic type Message object.
     * @param sender
     */
    @Override
    public MessageBuilder sessionHandling(IMessage message, String sender) {
        Optional<Object> messageObject;
        switch (this.sessionState) {
            case NoSession:
                if (checkTransferorState()) {
                    messageObject = Optional.ofNullable(this.identification.transferor(message, sender).orElse(this.sessionState.resetState()));
                }
                messageObject = Optional.of((createAdvertisement()));
                this.sessionState.nextState();

                messageBuilder = new MessageBuilder(messageObject, Channel.Advertisement.getChannelType(), sender);
                break;

            case Identification:
                if (checkTransferorState()) {
                    messageObject = Optional.ofNullable(this.identification.transferor(message, sender).orElse(this.sessionState.resetState()));
                } else {
                    messageObject = Optional.ofNullable(this.identification.transferee(message, sender).orElse(this.sessionState.resetState()));
                }
                if (messageObject.isPresent() && this.identification.sessionComplete(messageObject) ) {
                    this.identification.clearMessageList();
                    this.sessionState.nextState();
                }

                messageBuilder = new MessageBuilder(messageObject, Channel.Identification.getChannelType(), sender);
                break;

            case Request:
                if (checkTransferorState()) {
                    messageObject = Optional.ofNullable(this.request.transferor(message, sender).orElse(this.sessionState.resetState()));
                } else {
                    messageObject = Optional.ofNullable(this.request.transferee(message, sender).orElse(this.sessionState.resetState()));
                }
                if (messageObject.isPresent() && this.request.sessionComplete(messageObject) ) {
                    this.request.clearMessageList();
                    this.sessionState.nextState();
                }
                messageBuilder = new MessageBuilder(messageObject, Channel.Request.getChannelType(), sender);
                break;

            case Contract:
                if (checkTransferorState()) {
                    messageObject = Optional.ofNullable(this.contract.transferor(message, sender).orElse(this.sessionState.resetState()));
                } else {
                    messageObject = Optional.ofNullable(this.contract.transferee(message, sender).orElse(this.sessionState.resetState()));
                }
                if (messageObject.isPresent() && this.contract.sessionComplete(messageObject) ) {
                    DeviceState.Transferee.isActive();
                    resetAll();
                }


                 messageBuilder = new MessageBuilder(messageObject, Channel.Contract.getChannelType(), sender);
                break;

            default:
                System.err.println("There was no session flag: " + this.sessionState);
                resetAll();
                break;
        }
        return messageBuilder;
    }

    /**
     * Method to reset every list and current state.
     */
    public void resetAll() {
        this.identification.clearMessageList();
        this.request.clearMessageList();
        this.contract.clearMessageList();
        this.sessionState.resetState();
        Contract.contractSent = false;
    }
}
