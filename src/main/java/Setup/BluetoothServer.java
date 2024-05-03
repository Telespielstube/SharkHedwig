package Setup;

import Misc.Utilities;
import User.UserManager;
import javax.bluetooth.*;
import javax.microedition.io.*;
import java.io.*;

/**
 * The BluetoothServer class sets up an object that allow the component to receive the shipping label data from a nearby
 * bluetooth device e.g. a mobile Android device with the installed SharkHedwigWeb software. This class does not serve as a server
 * for P2P connections between two SharkHedwigComponents.
 *
 * The main source for this bluetooth server implementation is based on the Oracle Java APIs for Bluetooth:
 *
 * https://www.oracle.com/technical-resources/articles/javame/bluetooth-wireless-technology-part1.html
 * https://www.oracle.com/technical-resources/articles/javame/bluetooth-wireless-technology-part2.html
 */
public class BluetoothServer implements Runnable {

    private final UserManager userManager;
    private LocalDevice localDevice; // local Bluetooth Manager
    private DiscoveryAgent discoveryAgent; // discovery agent
    private StreamConnectionNotifier streamConnectionNotifier;
    private StreamConnection streamConnection;
    private static final String serviceName = "SharkHedwigService";
    private static final String serviceUUID = "f06b2657-0175-49f6-9dcc-8fc159661c6a";
    private final UUID UUID = new UUID(serviceUUID, false);
    private String connURL = "btspp://localhost:" + UUID.toString() + ";name=" + serviceName;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public BluetoothServer(UserManager userManager) {
        this.userManager = userManager;
    }
    /**
     * Initializes the SharkHedwigComponent as a bluetooth server. That means it offers a service to romete client devices.
     */
    public void init() throws BluetoothStateException {
        // Retrieve the local device to get to the Bluetooth Manager
        this.localDevice = LocalDevice.getLocalDevice();
        this.localDevice.setDiscoverable(DiscoveryAgent.GIAC); // No limit is set on how long the device is discoverable.
        // Clients retrieve the discovery agent
        this.discoveryAgent = localDevice.getDiscoveryAgent();
    }

    /**
     * Creates a connection its corresponding service record and makes it visible to remote devices.
     */
    public void register() {
        try {
            this.streamConnectionNotifier = (StreamConnectionNotifier) Connector.open(connURL);
            this.streamConnection = streamConnectionNotifier.acceptAndOpen();
        } catch (IOException e) {
            System.err.println(Utilities.formattedTimestamp() + "Caught an Exception while trying to connect to Bluetooth Low Energy device: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Connects to remote client devices and reads from the establieshed connection.
     */
    public void connect() {
        try {
            RemoteDevice remoteDevice = RemoteDevice.getRemoteDevice(this.streamConnection);
            remoteDevice.getFriendlyName(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads the incoming byte file as UTF-8 encoded string and sends it over to the UserManager for further processing.
     */
    public boolean read() {
        String shippingLabel = null;
        try {
            this.dataInputStream = this.streamConnection.openDataInputStream();
            shippingLabel = this.dataInputStream.readUTF();
        } catch (IOException e) {
            System.err.println(Utilities.formattedTimestamp() + "Caught an Exception while reading the incoming " +
                    "shipping label data: " + e.getMessage());
            return false;
        }
        close();
        userManager.processJson(shippingLabel);
        return true;
    }

    /**
     * Send a short information text to the sender of the shipping label data. Its
     * just an information that the crucial data was received.
     */
    public void write() {
        try {
            this.dataOutputStream = this.streamConnection.openDataOutputStream();
            this.dataOutputStream.writeBytes("Got shipping label data");
            this.dataOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        close();
    }

    /**
     * Closes a no longer needed bluetooth connection.
     */
    private void close() {
        try {
            this.streamConnection.close();
        } catch (IOException e) {
            System.err.println(Utilities.formattedTimestamp() + "Caught an Exception while closing the bluetooth connection: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     *
     */
    public void run() {
        while (true) {
            connect();
            if (read()) {
                write();
            }
        }
    }

}
