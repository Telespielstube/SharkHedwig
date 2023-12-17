package Session;

import DeliveryContract.DeliveryContract;
import Message.Identification.Challenge;
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
    private SessionState state;

    private Identification identification;
    private IMessageHandler messageHandler;
    private String sender;
    private Challenge messageObject;

    public SessionManager(MessageHandler messageHandler, ASAPPeer peer, SharkPKIComponent sharkPKIComponent) {
        this.messageHandler = messageHandler;
        this.peer = peer;
        this.sharkPKIComponent = sharkPKIComponent;

    }

    public <T> boolean handleIncoming(T message, String sender, SessionState state, DeviceState deviceState) {
        this.state = state;
        this.sender = sender;

        if (!DeliveryContract.contractCreated) {
            handleTransferee(message);
        } else {
            deviceState = DeviceState.Transferor.isActive();
            handleTransferor(message);
        }
        return true;
    }

    public <T> void handleTransferor(T message) {
        if (this.state.equals(SessionState.NoSession) && (((IMessage) message).getMessageFlag().equals(MessageFlag.Advertisement.getFlag()))) {
            try {
                this.identification = new Identification(sender, this.sharkPKIComponent);
                this.messageObject = identification.initSession();
            } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            this.state.nextState();
        }

        if (this.state.equals(SessionState.Identification)) {
                this.identification.unpackMessage(message);

            if (this.identification.isSessionComplete()) {
                this.state.nextState();
            }

        }
        if (this.state.equals(SessionState.Request)) {
        }
//        if (this.state.equals(SessionState.Contract)) {
//            if (((AbstractContract) message).getMessageFlag().equals(MessageFlag.Request.getFlag())) {
//            }
//            if (((AbstractContract) message).getMessageFlag().equals(MessageFlag.Confirmation.getFlag())) {
//                identification.parseMessage(message);
//            }

    }

    public <T> void handleTransferee(T message) {
//        if (this.state.equals(SessionState.Identification) && ((AbstractIdentification) message).getMessageFlag().equals(MessageFlag.Challenge.getFlag())) {
//            identification.parseMessage(message);
//        } else {
//            identification.parseMessage(message);
//        }
//        this.state.nextState();
//
//        else if( ((AbstractIdentification) message).getMessageFlag().equals(MessageFlag.Response.getFlag() ) ) {
//            new Response(((AbstractIdentification) message).getUuid(), ((Response) message).getResponseNumber(),
//                    ((Response) message).getMessageFlag(), ((Response) message).getTimestamp());
//        }
    }

    public void handleOutgoing() {
        byte[] signedByteMessage = this.messageHandler.buildOutgoingMessage(this.messageObject, Channel.Identification.getChannelType(), sender);
        try {
            this.peer.sendASAPMessage(Constant.AppFormat.getAppConstant(), Channel.Identification.getChannelType(), signedByteMessage);
        } catch (ASAPException e) {
            throw new RuntimeException(e);
        }
    }
}

