package Session;

import DeliveryContract.DeliveryContract;
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
    private Object messageObject;
    private Advertisement advertisement;

    public SessionManager() {}

    public SessionManager(MessageHandler messageHandler, SessionState sessionState, DeviceState isTransferor, ASAPPeer peer, SharkPKIComponent sharkPKIComponent) {
        this.messageHandler = messageHandler;
        this.peer = peer;
        this.sharkPKIComponent = sharkPKIComponent;
        this.isTransferor = isTransferor;
        this.sessionState = sessionState;
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
        if ( (DeliveryContract.contractCreated)) {
            this.isTransferor = DeviceState.Transferor;
            isTransferor = true;
        }
        else if (this.isTransferor.equals(DeviceState.Transferor.isActive())){
            isTransferor = true;
        }
        return isTransferor;
    }

    @Override
    public void handleTransferor(IMessage message, String sender) {
        this.sender = sender;

        switch (this.sessionState) {
            case NoSession:
                try {
                    this.identification = new Identification(this.sharkPKIComponent);
                    this.messageObject = identification.unpackMessage(message);
                    this.sessionState.nextState();
                } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
                break;
            case Identification:
                this.messageObject = this.identification.unpackMessage(message);
                if (this.identification.isSessionComplete()) {
                    this.sessionState.nextState();
                }
                break;
            case Request:
                this.request = new Request();
                this.messageObject = this.request.unpackMessage(message);
                if (this.request.isSessionComplete()) {
                    this.sessionState.nextState();
                }
                break;
            case Contract:
                this.messageObject = this.contract.unpackMessage(message);
                if (this.contract.isSessionComplete()) {
                    this.sessionState.nextState();
                }
                break;
            default:
                this.messageObject = null;
                break;
        }
        while (!this.messageObject.equals(null)) {
            handleOutgoing();
        }
    }
//        if (this.sessionState.equals(SessionState.NoSession) ) {
//            try {
//                this.identification = new Identification(this.sharkPKIComponent);
//                this.messageObject = identification.unpackMessage(message);
//                this.sessionState.nextState();
//            } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
//                e.printStackTrace();
//                throw new RuntimeException(e);
//            }
//        }
//        else if (this.sessionState.equals(SessionState.Identification)) {
//            this.messageObject = this.identification.unpackMessage(message);
//            if (this.identification.isSessionComplete()) {
//                this.sessionState.nextState();
//            }
//        }
//        else if (this.sessionState.equals(SessionState.Request)) {
//            this.request = new Request();
//            this.messageObject = this.request.unpackMessage(message);
//            if (this.request.isSessionComplete()) {
//                this.sessionState.nextState();
//            }
//        }
//
//        else if (this.sessionState.equals(SessionState.Contract)) {
//            this.messageObject = this.contract.unpackMessage(message);
//            if (this.contract.isSessionComplete()) {
//                this.sessionState.nextState();
//            }
//        }
//        while (!this.messageObject.equals(null)) {
//            handleOutgoing();
//        }
//    }

    @Override
    public <T> void handleTransferee(IMessage message, String sender) {
        this.sender = sender;
        if (this.sessionState.equals(SessionState.NoSession) ) {
            this.messageObject = createAdvertisement();
        } else if

        while (!messageObject.equals(null)) {
            handleOutgoing();
        }
    }

    @Override
    public void handleOutgoing() {
        byte[] signedByteMessage = this.messageHandler.buildOutgoingMessage(this.messageObject, Channel.Identification.getChannelType(), sender);
        try {
            this.peer.sendASAPMessage(Constant.AppFormat.getAppConstant(), Channel.Identification.getChannelType(), signedByteMessage);
        } catch (ASAPException e) {
            throw new RuntimeException(e);
        }
    }
}
