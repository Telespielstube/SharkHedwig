package Setup;

import DeliveryContract.DeliveryContract;
import DeliveryContract.ShippingLabel;
import Misc.ErrorLogger;
import net.sharksystem.pki.SharkPKIComponentFactory;
import net.sharksystem.SharkException;
import net.sharksystem.SharkPeerFS;
import net.sharksystem.asap.*;
import net.sharksystem.pki.SharkPKIComponent;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;
import java.util.Observer;
import java.util.Optional;

import Misc.Logger;
import Message.*;
import Session.*;

import javax.crypto.NoSuchPaddingException;

public class SharkHedwigComponent implements ISharkHedwigComponent, ASAPMessageReceivedListener {

    private ASAPPeer peer;
    private SharkPKIComponent sharkPKIComponent;
    private final MessageHandler messageHandler;
    private ISessionManager sessionManager;
    private ProtocolState protocolState;
    private final SharkPeerFS sharkPeerFS;
    private ASAPMessages messages;
    private ShippingLabel shippingLabel = new ShippingLabel();
    private DeliveryContract deliveryContract = new DeliveryContract("", null);

    public SharkHedwigComponent(SharkPKIComponent pkiComponent) throws NoSuchPaddingException, NoSuchAlgorithmException {
        ErrorLogger.redirectErrorStream(AppConstant.PeerFolder.toString(), AppConstant.LogFolder.toString(), "errorLog.txt");
        this.sharkPeerFS = new SharkPeerFS(AppConstant.PeerName.toString(), AppConstant.PeerFolder.toString() + "/" + AppConstant.PeerName.toString() );
        this.sharkPKIComponent = pkiComponent;
        this.messageHandler = new MessageHandler();

    }

    /**
     * Setup process for the SkarkPKIComponent.
     *
     * @param sharkPeerFS SharkPeerFS Object that the PKIComponent can be created.
     * @throws SharkException Is thrown if something goes wrong during the setup process.
     */
    public void setupComponent(SharkPeerFS sharkPeerFS) {
        SharkPKIComponentFactory sharkPkiComponentFactory = new SharkPKIComponentFactory();
        try {
            sharkPeerFS.addComponent(sharkPkiComponentFactory, SharkPKIComponent.class);
        } catch (SharkException e) {
            System.err.println("Caught an IOException: " + e.getMessage());
        }
        ComponentFactory componentFactory;
        try {
            componentFactory = new ComponentFactory((SharkPKIComponent) sharkPeerFS.getComponent(SharkPKIComponent.class));
            sharkPeerFS.addComponent(componentFactory, ISharkHedwigComponent.class);
            // SharkPKIComponent is an Interface therefore --> Conversion from an interface type to a class type requires an explicit cast to the class type.
            this.sharkPKIComponent = (SharkPKIComponent) sharkPeerFS.getComponent(SharkPKIComponent.class);
            this.sharkPeerFS.start();
        } catch (SharkException e) {
            System.err.println("Caught an Exception: " + e);
            throw new RuntimeException(e);
        }
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
        this.peer.addASAPMessageReceivedListener(AppConstant.AppFormat.toString(), this);
        try {
            this.peer.setASAPRoutingAllowed(AppConstant.AppFormat.toString(), true);
            this.setupChannel();
            this.peer.getASAPStorage(AppConstant.AppFormat.toString()).getOwner();
            this.setupLogger();
        } catch (IOException e) {
            System.err.println("Caught an IOException while setting up component: " + e.getMessage());
        }
        new PKIManager(sharkPKIComponent);
        try {
            this.sessionManager = new SessionManager(SessionState.NoSession, ProtocolState.Transferee , this.peer, this.sharkPKIComponent);
            shippingLabel.addObserver((Observer) this.sessionManager);
            deliveryContract.addObserver((Observer) this.shippingLabel);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            System.err.println("Caught an Exception: " + e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Setting up all component channels. Multiple channels allow us to control incoming and outgoing messages much better.
     */
    public void setupChannel()  {
        for (Channel type : Channel.values()) {
            try {
                this.peer.getASAPStorage(AppConstant.AppFormat.toString()).createChannel(type.getChannel());
            } catch (IOException | ASAPException e) {
                System.err.println("Caught an Exception: " + e);
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Setting up all things logging. The folder and the files to differentiate between request session and contract session.
     */
    public void setupLogger() {
        String[] directories = { AppConstant.RequestLog.toString(), AppConstant.DeliveryContract.toString() };
        try {

            for (String directory : directories) {
                Logger.createLogDirectory(AppConstant.PeerFolder.toString(), AppConstant.LogFolder.toString(), directory);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create logger files for request and contract sessions: " + e);
        }
    }

    /**
     * If an encounter occurs all messages are being exchanged and this method is called to process them.
     *
     * @param messages               a chunk of messages from the encountered device.
     * @param senderE2E              The encountered device.
     * @param list                   A List of all previous hops.
     * @throws IOException           Is thrown after a read/write error occurs
     *
     */
    public void asapMessagesReceived(ASAPMessages messages, String senderE2E, List<ASAPHop> list) throws IOException {
        CharSequence uri = messages.getURI();
        if ( uri.equals(Channel.Advertisement.getChannel()) ) {
            processMessages(messages, senderE2E);
        } else if ( (uri.equals(Channel.Identification.getChannel()))) {
            processMessages(messages, senderE2E);
        } else if (uri.equals(Channel.Request.toString()))  {
            processMessages(messages, senderE2E);
        } else if (uri.equals(Channel.Contract.toString()))  {
            processMessages(messages, senderE2E);
        }
        System.err.println("Message has invalid format: ");
    }

    /**
 * The methode parses the byte[] message to a Message object and passes it to the SEssionManager class.
     *
     * @param messages    received message as byte[] data type..
     * @param senderE2E   The device which sent the received message.
     * @return
     * @throws IOException
     */
    public void processMessages(ASAPMessages messages, String senderE2E) {
        IMessage message;
        IMessage messageObject;
        byte[] encryptedMessage;
        try {
            for (Iterator<byte[]> it = messages.getMessages(); it.hasNext(); ) {
                if (!messageHandler.checkRecipient(it.next())) {
                    continue;
                }
                message = (IMessage) this.messageHandler.parseMessage(it.next(), senderE2E, sharkPKIComponent);
                messageObject = (IMessage) MessageHandler.byteArrayToObject(it.next());
                Optional<MessageBuilder> messageBuilder = sessionManager.sessionHandling(messageObject, senderE2E);
                if (!messageBuilder.isPresent()) {
                    continue;
                }
                encryptedMessage = messageHandler.buildOutgoingMessage(messageBuilder.get().getMessage(),
                        messageBuilder.get().getUri(), messageBuilder.get().getSender(), sharkPKIComponent);
                try {
                    this.peer.sendASAPMessage(AppConstant.AppFormat.toString(), AppConstant.Scheme +
                            messageBuilder.get().getUri(), encryptedMessage);
                } catch (ASAPException e) {
                    System.err.println("Caught an Exception: " + e);
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            System.err.println("Caught an Exception: " + e);
            throw new RuntimeException(e);
        }
    }
}
