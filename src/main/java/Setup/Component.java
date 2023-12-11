package Setup;

import Message.IMessageHandler;
import Message.MessageHandler;
import Session.Identification;
import Session.SessionManager;
import net.sharksystem.pki.SharkPKIComponentFactory;
import net.sharksystem.SharkComponent;
import net.sharksystem.SharkException;
import net.sharksystem.SharkPeerFS;
import net.sharksystem.asap.*;
import net.sharksystem.pki.SharkPKIComponent;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;

import Channel.*;
import Misc.SessionLogger;
import static Misc.Constants.*;

public class Component implements SharkComponent, ASAPMessageReceivedListener {

    private ASAPPeer peer;
    private SharkPKIComponent sharkPKIComponent;
    private Channel channel;
    private IMessageHandler messageHandler;
    private byte[] deserialized;

    public Component() {}
    public Component(SharkPKIComponent pkiComponent) {
        this.sharkPKIComponent = pkiComponent;
        this.channel = new Channel();
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
            this.peer.setASAPRoutingAllowed(APP_FORMAT, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.peer.addASAPMessageReceivedListener(APP_FORMAT, this);
        try {
            this.setupChannel();
            this.peer.getASAPStorage(APP_FORMAT).getOwner();
            this.setupLogger();
        } catch (IOException e) {
            System.err.println("Caught an IOException while setting up component: " + e.getMessage());
        }
        new PKIManager(CA_ID, sharkPKIComponent);
    }


    /**
     * Setting up all component channels. Multiple channels allow us to better control incoming and outgoing messages.
     */
    private void setupChannel() throws RuntimeException, IOException, ASAPException {
        for (Type type : Type.values()) {
            this.peer.getASAPStorage(APP_FORMAT).createChannel(this.channel.getChannelSchema() + type);
        }
    }

    /**
     * Setting up all things logging. The folder and the files to differentiate between request session and contract session.
     */
    private void setupLogger() {
        String[] files = {REQUEST_LOGFILE, CONTRACT_LOGFILE};
        try {
            SessionLogger.createLogDirectory();
            for (String logFile : files) {
                SessionLogger.createLoggerFile(logFile);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create logger files for request and contract sessions: " + e);
        }
    }

    @Override
    public void asapMessagesReceived(ASAPMessages messages, String senderE2E, List<ASAPHop> list) throws IOException , ClassCastException {
        CharSequence uri = messages.getURI();
        this.messageHandler = new MessageHandler(sharkPKIComponent, this.peer);

        if (uri != null) {
            if (uri.equals(Type.IDENTIFICATION.toString())) {
                for (Iterator<byte[]> it = messages.getMessages(); it.hasNext(); ) {
                    try {
                        this.deserialized = this.messageHandler.deserializeMessage(it.next());
                    } catch (ASAPException e) {
                        throw new RuntimeException(e);
                    }
                    if (this.sharkPKIComponent.isOwner(this.messageHandler.getReceiver())) {
                        this.messageHandler.decryptMessage(this.deserialized);
                    }
                    // Handle sender and hopList
                }
            }
            if (uri.equals(Type.REQUEST.toString())) {
                for (Iterator<byte[]> it = messages.getMessages(); it.hasNext(); ) {
                    try {
                        this.deserialized = this.messageHandler.deserializeMessage(it.next());
                    } catch (ASAPException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } else {
            System.err.println("Received message has no uri!");
        }
    }
}
