package Setup;

import DeliveryContract.DeliveryContract;
import DeliveryContract.ShippingLabel;
import User.UserManager;
import Message.*;
import ProtocolRole.ProtocolRole;
import Session.SessionManager;
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
import java.util.Iterator;
import java.util.List;
import java.util.Observer;
import java.util.Optional;
import Misc.*;
import Session.ISessionManager;
import Battery.*;
import Location.GeoSpatial;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class SharkHedwigComponent implements ASAPMessageReceivedListener, SharkComponent {
    private ASAPPeer peer;
    private SharkPKIComponent sharkPKIComponent;
    private final MessageHandler messageHandler;
    private ISessionManager sessionManager;
    private final SharkPeerFS sharkPeerFS;
    private final ShippingLabel shippingLabel = new ShippingLabel.Builder(null,null,null, null,
            null, null, null, null).build();
    private final DeliveryContract deliveryContract = new DeliveryContract();
    private final UserManager userManager;
    private final Battery battery;
    private final GeoSpatial geoSpatial;
    private ProtocolRole protocolRole;
    private Advertiser advertisementThread;

    /**
     * The component implements a decentralized protocol that allows drones to transport a physical package from a
     * starting point to a destination. To make the package hand-over process more secure and traceable the component
     * includes the SharkPKIComponent for certificate exchange between the drones.
     *
     * Created by Martina Brüning
     */
    public SharkHedwigComponent() {
        this.sharkPeerFS = new SharkPeerFS(AppConstant.PEER_NAME.toString(), AppConstant.PEER_FOLDER + "/" + AppConstant.PEER_NAME);
        this.messageHandler = new MessageHandler();
        this.battery = new BatteryManager();
        this.geoSpatial = new GeoSpatial();
        this.userManager = new UserManager();
        setupComponent();
    }

    /**
     * Setup process to add the SkarkPKIComponent and to the SharkPeer platform.
     *
     * @throws SharkException    Is thrown if something goes wrong during the setup process.
     */
    public void setupComponent() {
        SharkPKIComponentFactory sharkPkiComponentFactory = new SharkPKIComponentFactory();
        try {
            setupErrorStream();
            sharkPeerFS.addComponent(sharkPkiComponentFactory, SharkPKIComponent.class);
            this.sharkPKIComponent = (SharkPKIComponent) this.sharkPeerFS.getComponent(SharkPKIComponent.class);
            this.sharkPeerFS.start();
        } catch (SharkException | IOException e) {
            System.err.println(Utilities.formattedTimestamp() + "Caught an Exception while setting up the SharkComponent: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * This methode is called from within the methode SharkPeerFS.start() to get the component
     * and its setup process started.
     * <p>
     * ASAPPeer  Describes the actual Peer
     */
    @Override
    public void onStart(ASAPPeer asapPeer)  {
        this.peer = asapPeer;
        this.peer.addASAPMessageReceivedListener(AppConstant.APP_FORMAT.toString(), this);
        try {
            this.peer.setASAPRoutingAllowed(AppConstant.APP_FORMAT.toString(), true);
            setupChannel(AppConstant.APP_FORMAT.toString());
            this.peer.getASAPStorage(AppConstant.APP_FORMAT.toString()).getOwner();
            setupLogger();
        } catch (IOException | ASAPException e) {
            System.err.println(Utilities.formattedTimestamp() + "Caught an IOException while setting up component:   " + e.getMessage());
            throw new RuntimeException(e);
        }
        new PKIManager(sharkPKIComponent);
        this.protocolRole = new ProtocolRole(this.shippingLabel, this.deliveryContract, this.battery,
                this.geoSpatial, this.sharkPKIComponent);
        this.sessionManager = new SessionManager(this.shippingLabel, this.protocolRole, this.deliveryContract);
        shippingLabel.addObserver((Observer) this.sessionManager);
        deliveryContract.addObserver((Observer) this.sessionManager);

        new Advertiser(this, this.protocolRole).run();
    }

    /**
     * Setting up all component channels. Multiple channels allow us to control incoming and outgoing messages much better.
     */
    public void setupChannel(String channelName)  {
        for (Channel type : Channel.values()) {
            try {
                this.peer.getASAPStorage(channelName).createChannel(type.getChannel());
            } catch (IOException | ASAPException e) {
                System.err.println(Utilities.formattedTimestamp() + "Caught an Exception while setting up the messaging channels: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * To make the occurring error messages persistent. Every occuring error is redirected to a file on the device.
     */
    public void setupErrorStream() throws IOException {
        try {
            Files.createDirectories(Paths.get(AppConstant.ERROR_LOG_PATH.toString()));
            System.setErr(new PrintStream(Files.newOutputStream(Paths.get(AppConstant.ERROR_LOG_PATH + "/"
                    + AppConstant.ERROR_LOG_FILE), CREATE, APPEND)));
        } catch (IOException e) {
            System.err.println(Utilities.formattedTimestamp() + "Caught an Exception while creating the log folders: " + e.getMessage());
            throw new RuntimeException(e);
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
     * If an encounter occurs all messages are being exchanged and this listener method is called to process them.
     *
     * @param messages               a chunk of messages from the encountered device.
     * @param senderE2E              The encountered device.
     * @param list                   A List of all previous hops.
     */
    public void asapMessagesReceived(ASAPMessages messages, String senderE2E, List<ASAPHop> list) {
        CharSequence uri = messages.getURI();
        if (!uri.equals(Channel.NO_SESSION.getChannel()) || uri.equals(Channel.REQUEST.toString()) || uri.equals(Channel.CONTRACT.toString())) {
            try {
                for (Iterator<byte[]> it = messages.getMessages(); it.hasNext(); ) {
                    if (!messageHandler.checkRecipient(it.next())) {
                        continue;
                    }
                    Messageable message = (Messageable) this.messageHandler.parseIncomingMessage(it.next(), senderE2E, sharkPKIComponent);
                    Optional<MessageBuilder> messageBuilder = this.sessionManager.sessionHandling(message, senderE2E);
                    outgoingMessage(messageBuilder);
                }
            } catch (IOException e) {
                System.err.println(Utilities.formattedTimestamp() + "Caught an IOException while iterating trough th messages: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
        System.err.println(Utilities.formattedTimestamp() + " Message has invalid uri format: " + uri );
    }

    public Optional<MessageBuilder> testMethod(Messageable message, String senderE2E) {
        return this.sessionManager.sessionHandling(message, senderE2E);

    }
    /**
     * This is only a little helper method to bypass the non-functioning parseIncomingMessage().
     * Needs to be deleted as soon as the mentioned method works.
     * @param message
     * @param senderE2E
     * @return
     */
    public Optional<MessageBuilder> testHelperMethod(Messageable message, String senderE2E) {
        return this.sessionManager.sessionHandling(message, senderE2E);

    }

    /**
     * Handles the built message object to be send to peers during the next encounter.
     *
     * @param messageBuilder    MessageBuilder object encapsulated in an Optional container object.
     */
    public void outgoingMessage(Optional<MessageBuilder> messageBuilder) {
        messageBuilder.ifPresent(object -> {
            byte[] encryptedMessage = messageHandler.buildOutgoingMessage(object.getMessage(), object.getUri(),
                    object.getSender(), sharkPKIComponent);
            try {
                this.peer.sendASAPMessage(AppConstant.APP_FORMAT.toString(), AppConstant.SCHEME +
                        object.getUri(), encryptedMessage);
            } catch (ASAPException e) {
                System.err.println(Utilities.formattedTimestamp() + "Caught an ASAPException while trying to send a message: " + e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }
}
