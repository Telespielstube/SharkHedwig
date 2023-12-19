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
    private DeviceState deviceState;

    public Component() {}
    public Component(SharkPKIComponent pkiComponent) {
        this.sharkPKIComponent = pkiComponent;
        this.messageHandler = new MessageHandler();
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
        this.sessionManager = new SessionManager(this.messageHandler, SessionState.NoSession, DeviceState.Transferee ,this.peer, this.sharkPKIComponent);

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
        } catch (IOException e) {
            System.err.println("Caught an IOException while setting up component: " + e.getMessage());
        }
        new PKIManager(Constant.CaId.getAppConstant(), sharkPKIComponent);
    }

    /**
     * Setting up all component channels. Multiple channels allow us to control incoming and outgoing messages much better.
     */
    private void setupChannel()  {
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
    private void setupLogger() {
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
        while (uri != null) {
            if ( uri.equals(Channel.Advertisement.getChannelType()) ) {
                processMessages(messages, senderE2E);
            } else if ( (uri.equals(Channel.Identification.getChannelType()))) {
                processMessages(messages, senderE2E);
            } else if (uri.equals(Channel.Request.toString()))  {
                processMessages(messages, senderE2E);
            } else if (uri.equals(Channel.Contract.toString()))  {
                processMessages(messages, senderE2E);
            }
            System.err.println("Message has invalid format: ");
        }
        System.err.println("Received message has no uri!");
        }

    /**
     * The methode parses the byte[] message to a Message object and passes it to the SEssionManager class.
     *
     * @param messages    received message as byte[] data type..
     * @param senderE2E   The device which sent the received message.
     * @return
     * @throws IOException
     */
    private void processMessages(ASAPMessages messages, String senderE2E) {
        IMessage message;
        try {
            for (Iterator<byte[]> it = messages.getMessages(); it.hasNext(); ) {
                message = this.messageHandler.parseMessage(it.next(), senderE2E, sharkPKIComponent);
                sessionManager.sessionHandling(message, senderE2E);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendASAPMessage(Channel uri, byte[] signedMessage) throws ASAPException {
        this.peer.sendASAPMessage(Constant.AppFormat.getAppConstant(), uri.toString(), signedMessage);
    }

}
