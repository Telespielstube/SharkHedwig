package Session;

import DeliveryContract.DeliveryContract;
import Misc.LogEntry;
import Misc.SessionLogger;
import Misc.Utilities;
import Setup.Channel;
import Setup.Constant;
import Setup.DeviceState;
import Message.*;
import Session.Sessions.*;
import net.sharksystem.asap.ASAPException;
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
    private Optional<Object> messageObject;
    private Advertisement advertisement;
    private LogEntry logEntry;

    public SessionManager() {}

    public SessionManager(MessageHandler messageHandler, SessionState sessionState, DeviceState isTransferor, ASAPPeer peer, SharkPKIComponent sharkPKIComponent) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.messageHandler = messageHandler;
        this.peer = peer;
        this.sharkPKIComponent = sharkPKIComponent;
        this.isTransferor = isTransferor;
        this.sessionState = sessionState;
        this.identification = new Identification(this.sharkPKIComponent);
        this.request = new Request();
        this.contract = new Contract();
        this.logEntry = new LogEntry();
    }

    /**
     * A small message just to advertise a delivery service.
     *
     * @return    Advertisement message object.
     */
    private Advertisement createAdvertisement() {
        return new Advertisement(this.advertisement.createUUID(), true, MessageFlag.Advertisement, Utilities.createTimestamp());
    }

    @Override
    public boolean checkTransferorState() {
        boolean isTransferor = false;
        if (DeliveryContract.contractCreated) {
            this.isTransferor = DeviceState.Transferor.isActive();
            isTransferor = true;
        }
        return isTransferor;
    }

    @Override
    public void sessionHandling(IMessage message, String sender) {
        this.sender = sender;

        switch (this.sessionState) {
            case NoSession:
                if (checkTransferorState()) {
                    this.messageObject = Optional.ofNullable(this.identification.transferor(message).orElse(this.sessionState.resetState()));
                }
                this.messageObject = Optional.of(createAdvertisement());
                this.sessionState.nextState();
                break;

            case Identification:
                if (checkTransferorState()) {
                    this.messageObject = Optional.ofNullable(this.identification.transferor(message).orElse(this.sessionState.resetState()));
                }
                this.messageObject = Optional.ofNullable(this.identification.transferee(message).orElse(this.sessionState.resetState()));
                break;

            case Request:
                if (checkTransferorState()) {
                    this.messageObject = Optional.ofNullable(this.request.transferor(message).orElse(this.sessionState.resetState()));
                }
                this.messageObject = Optional.ofNullable(this.request.transferee(message).orElse(this.sessionState.resetState()));
                if (SessionLogger.writeEntry(null, Constant.RequestLogPath.getAppConstant())) {
                    this.request.sessionComplete();
                    this.sessionState.nextState();
                }
                break;

            case Contract:
                if (checkTransferorState()) {
                    this.messageObject = identification.transferor(message);
                } else {
                    this.messageObject = identification.transferee(message);
                }
                if (SessionLogger.writeEntry(null, Constant.ContractLog.getAppConstant())) {
                    this.request.sessionComplete();
                    this.sessionState.nextState();
                }
                break;
            default:
                break;
        }
        handleOutgoing();
    }


    @Override
    public void handleOutgoing() {
        byte[] signedByteMessage = this.messageHandler.buildOutgoingMessage(this.messageObject, "uri", sender);
        try {
            this.peer.sendASAPMessage(Constant.AppFormat.getAppConstant(), Channel.Identification.getChannelType(), signedByteMessage);
        } catch (ASAPException e) {
            throw new RuntimeException(e);
        }
    }
}
