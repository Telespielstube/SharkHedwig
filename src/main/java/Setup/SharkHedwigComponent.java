package Setup;


import DeliveryContract.DeliveryContract;
import DeliveryContract.ShippingLabel;
import net.sharksystem.SharkComponent;
import net.sharksystem.pki.SharkPKIComponentFactory;
import net.sharksystem.SharkException;
import net.sharksystem.SharkPeerFS;
import net.sharksystem.asap.*;
import net.sharksystem.pki.SharkPKIComponent;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;
import java.util.Observer;
import java.util.Optional;

import Misc.Logger;
import Message.*;
import Session.*;
import Battery.*;
import Location.*;

import javax.crypto.NoSuchPaddingException;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class SharkHedwigComponent implements ASAPMessageReceivedListener, SharkComponent {

    private ASAPPeer peer;
    private SharkPKIComponent sharkPKIComponent;
    private final MessageHandler messageHandler;
    private ISessionManager sessionManager;
    private final SharkPeerFS sharkPeerFS;
    private ASAPMessages messages;
    private ShippingLabel shippingLabel = new ShippingLabel(null,null,null, null,
            null, null, null, 0.0);
    private DeliveryContract deliveryContract = new DeliveryContract();
    private ReceivedMessageList receivedMessageList = new ReceivedMessageList();
    private IBattery battery;
    private Locationable geoSpatial;

    /**
     * The component implements a decentralized protocol that allows drones to transport a physical package from a
     * starting point to a destination. To make the package hand-over process more secure and traceable the component
     * includes the SharkPKIComponent for certificate exchange between the drones.
     *
     * Created my Martina Brüning
     *
     * @param pkiComponent                  Reference to the SharkPKICOmponent.
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public SharkHedwigComponent(SharkPKIComponent pkiComponent) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException {
        System.setErr(new PrintStream(Files.newOutputStream(Paths.get(AppConstant.ERROR_LOG_PATH.toString()), CREATE, APPEND)));
        this.sharkPeerFS = new SharkPeerFS(AppConstant.PEER_NAME.toString(), AppConstant.PEER_FOLDER.toString() + "/" + AppConstant.PEER_NAME.toString() );
        this.sharkPKIComponent = pkiComponent;
        this.messageHandler = new MessageHandler();
        this.battery = new Battery();
        this.geoSpatial = new GeoSpatial();
        setupComponent(sharkPeerFS);
    }

    /**
     * Setup process for the SkarkPKIComponent.
     *
     * @param sharkPeerFS SharkPeerFS Object that the PKIComponent can be created.
     * @throws SharkException Is thrown if something goes wrong during the setup process.
     */
    public void setupComponent(SharkPeerFS sharkPeerFS) {
        SharkPKIComponentFactory sharkPkiComponentFactory = new SharkPKIComponentFactory();
        SharkHedwigComponentFactory sharkHedwigComponentFactory;
        try {
            sharkPeerFS.addComponent(sharkPkiComponentFactory, SharkPKIComponent.class);
            this.sharkPKIComponent = (SharkPKIComponent) sharkPeerFS.getComponent(SharkPKIComponent.class);
            sharkHedwigComponentFactory = new SharkHedwigComponentFactory((SharkPKIComponent)
                    sharkPeerFS.getComponent(SharkPKIComponent.class));
            sharkPeerFS.addComponent(sharkHedwigComponentFactory, SharkComponent.class);

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
        this.peer.addASAPMessageReceivedListener(AppConstant.APP_FORMAT.toString(), this);
        try {
            this.peer.setASAPRoutingAllowed(AppConstant.APP_FORMAT.toString(), true);
            this.setupChannel();
            this.peer.getASAPStorage(AppConstant.APP_FORMAT.toString()).getOwner();
            this.setupLogger();
        } catch (IOException e) {
            System.err.println("Caught an IOException while setting up component:   " + e.getMessage());
        }
        new PKIManager(sharkPKIComponent);
        try {
            this.sessionManager = new SessionManager(SessionState.NO_SESSION, ProtocolState.TRANSFEREE, this.receivedMessageList, this.battery, this.geoSpatial, this.sharkPKIComponent);
            shippingLabel.addObserver((Observer) this.sessionManager);
            deliveryContract.addObserver((Observer) this.sessionManager);

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
                this.peer.getASAPStorage(AppConstant.APP_FORMAT.toString()).createChannel(type.getChannel());
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
        String[] directories = { AppConstant.REQUEST_LOG_PATH.toString(), AppConstant.DELIVERY_CONTRACT_LOG_PATH.toString() };
        for (String directory : directories) {
            Logger.createLogDirectory(directory);
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
    public void asapMessagesReceived(ASAPMessages messages, String senderE2E, List<ASAPHop> list)      throws IOException {
        CharSequence uri = messages.getURI();
        if ( uri.equals(Channel.NO_SESSION.getChannel()) ) {
            processMessages(messages, senderE2E);
        } else if (uri.equals(Channel.REQUEST.toString()))  {
            processMessages(messages, senderE2E);
        } else if (uri.equals(Channel.CONTRACT.toString()))  {
            processMessages(messages, senderE2E);
        }
        System.err.println("Message has invalid format: " + uri);
    }

    /**
     * The methode parses the byte[] message to a Message object and passes it to the SEssionManager class.
     *
     * @param messages    received message as byte[] data type.
     * @param senderE2E   The device which sent the received message.
     * @return
     * @throws IOException
     */
    public void processMessages(ASAPMessages messages, String senderE2E) {
        try {
            for (Iterator<byte[]> it = messages.getMessages(); it.hasNext(); ) {
                if (!messageHandler.checkRecipient(it.next())) {
                    continue;
                }
                Messageable message = (Messageable) this.messageHandler.parseIncomingMessage(it.next(), senderE2E, sharkPKIComponent);
                Optional<MessageBuilder> messageBuilder = sessionManager.sessionHandling(message, senderE2E);
                messageBuilder.ifPresent(object -> {
                    byte[] encryptedMessage = messageHandler.buildOutgoingMessage(object.getMessage(), object.getUri(),
                            object.getSender(), sharkPKIComponent);
                    try {
                        this.peer.sendASAPMessage(AppConstant.APP_FORMAT.toString(), AppConstant.SCHEME +
                                object.getUri(), encryptedMessage);
                    } catch (ASAPException e) {
                        System.err.println("Caught an Exception: " + e);
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (IOException e) {
            System.err.println("Caught an IOException: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
