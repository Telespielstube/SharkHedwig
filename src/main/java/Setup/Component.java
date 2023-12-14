package Setup;


import DeliveryContract.DeliveryContract;
import net.sharksystem.pki.SharkPKIComponentFactory;
import net.sharksystem.SharkComponent;
import net.sharksystem.SharkException;
import net.sharksystem.SharkPeerFS;
import net.sharksystem.asap.*;
import net.sharksystem.pki.SharkPKIComponent;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import Misc.SessionLogger;
import Message.*;
import Session.*;

public class Component implements SharkComponent, ASAPMessageReceivedListener {

    private ASAPPeer peer;
    private SharkPKIComponent sharkPKIComponent;
    private MessageHandler messageHandler;
    private ISessionManager sessionManager;
    private SessionState sessionState;
    private DeviceState deviceState;

    public Component() {}
    public Component(SharkPKIComponent pkiComponent) {
        this.sharkPKIComponent = pkiComponent;
        this.messageHandler = new MessageHandler();
        this.sessionManager = new SessionManager(this.messageHandler, this.peer, this.sharkPKIComponent);
    }

    /**
     * Setup process for the SkarkPKIComponent.
     *
     * @param sharkPeerFS SharkPeerFS Object that the PKIComponent can be created.
     * @throws SharkException Is thrown if something goes wrong during the setup process.
     */
    public void setupComponent(SharkPeerFS sharkPeerFS) throws SharkException {
        SharkPKIComponentFactory sharkPkiComponentFactory = new SharkPKIComponentFactory();
        try {
            sharkPeerFS.addComponent(sharkPkiComponentFactory, SharkPKIComponent.class);
        } catch (SharkException e) {
            System.err.println("Caught an IOException: " + e.getMessage());
        }
        ComponentFactory componentFactory = new ComponentFactory((SharkPKIComponent) sharkPeerFS.getComponent(SharkPKIComponent.class));
        sharkPeerFS.addComponent(componentFactory, ComponentInterface.class);

        // SharkPKIComponent is an Interface therefore --> Conversion from an interface type to a class type requires an explicit cast to the class type.
        this.sharkPKIComponent = (SharkPKIComponent) sharkPeerFS.getComponent(SharkPKIComponent.class);
    }

    /**
     * This methode is called from within the methode SharkPeerFS to get the component
     * and its setup process started.
     * <p>
     * ASAPPeer  Describes the actual Peer
     */
    @Override
    public void onStart(ASAPPeer asapPeer) throws SharkException {
        this.peer = asapPeer;
        try {
            this.peer.setASAPRoutingAllowed(Constant.AppFormat.getAppConstant(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.peer.addASAPMessageReceivedListener(Constant.AppFormat.getAppConstant(), this);
        try {
            this.setupChannel();
            this.peer.getASAPStorage(Constant.AppFormat.getAppConstant()).getOwner();
            this.setupLogger();

            // Set the states for the session and the device.
            this.sessionState = SessionState.NoSession.currentState();
            if (!DeliveryContract.contractCreated) {
                this.deviceState = DeviceState.Transferee.isActive();
            }
            this.deviceState = DeviceState.Transferor.isActive();
        } catch (IOException e) {
            System.err.println("Caught an IOException while setting up component: " + e.getMessage());
        }
        new PKIManager(Constant.CaId.getAppConstant(), sharkPKIComponent);
    }

    /**
     * Setting up all component channels. Multiple channels allow us to better control incoming and outgoing messages.
     */
    public void setupChannel()  {
        for (Channel type : Channel.values()) {
            try {
                this.peer.getASAPStorage(Constant.AppFormat.getAppConstant()).createChannel(type.getChannelType());
            } catch (IOException | ASAPException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Setting up all things logging. The folder and the files to differentiate between request session and contract session.
     */
    public void setupLogger() {
        String[] files = { Constant.RequestLog.getAppConstant(), Constant.ContractLog.getAppConstant() };
        try {
            SessionLogger.createLogDirectory(Constant.PeerFolder.getAppConstant(), Constant.LogFolder.getAppConstant());
            for (String logFile : files) {
                SessionLogger.createLogFile(Constant.PeerFolder.getAppConstant(), Constant.LogFolder.getAppConstant(), logFile);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create logger files for request and contract sessions: " + e);
        }
    }

    /**
     * If an encounter occurs all messages are being exchanged.
     *
     * @param messages               a chunk of messages from the encountered device.
     * @param senderE2E              The encountered device.
     * @param list                   A List of all previous hops.
     * @throws IOException           Is thrown after a read/write error occurs
     *
     */
    @Override
    public void asapMessagesReceived(ASAPMessages messages, String senderE2E, List<ASAPHop> list) throws IOException {
        CharSequence uri = messages.getURI();
        boolean invalid = false;
        while (uri != null) {
            if ( uri.equals(Channel.Advertisement.getChannelType()) ) {
                invalid = isInvalid(messages, senderE2E, invalid);
            }
            if ( (uri.equals(Channel.Identification.getChannelType()))) {
                invalid = isInvalid(messages, senderE2E, invalid);
            }
            if (uri.equals(Channel.Request.toString()))  {
                invalid = isInvalid(messages, senderE2E, invalid);
            }
            if (uri.equals(Channel.Contract.toString()))  {
                invalid = isInvalid(messages, senderE2E, invalid);
            }
            System.err.println("Message has invalid format" + invalid);
        }
        System.err.println("Received message has no uri!");
        }

    private boolean isInvalid(ASAPMessages messages, String senderE2E, boolean invalid) throws IOException {
        IMessage message;
        for (Iterator<byte[]> it = messages.getMessages(); it.hasNext(); ) {
            message = this.messageHandler.parseMessage(it.next(), senderE2E, sharkPKIComponent);
            if (!this.sessionManager.handleSession(message, senderE2E, this.sessionState, this.deviceState)) {
                invalid = true;
            }
        }
        return invalid;
    }

    public void sendASAPMessage(Channel uri, byte[] signedMessage) throws ASAPException {
        this.peer.sendASAPMessage(Constant.AppFormat.getAppConstant(), uri.toString(), signedMessage);

    }

}
