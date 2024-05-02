package Setup;

import Misc.Utilities;

import javax.bluetooth.*;
import javax.microedition.io.*;
import java.io.IOException;

/**
 * The BluetoothServer class sets up an object that allow the component to receive the shipping label data from a nearby
 * bluetooth device e.g. a mobile Android device with the installed SharkHedwigWeb software.
 */
public class BluetoothServer implements Runnable {

    private LocalDevice localDevice; // local Bluetooth Manager
    private DiscoveryAgent discoveryAgent; // discovery agent
    private static final String serviceName = "SharkHedwigService";
    private static final String serviceUUID = "f06b2657-0175-49f6-9dcc-8fc159661c6a";
    private final UUID MYSERVICEUUID_UUID = new UUID(serviceUUID, false);

    String connURL = "btspp://localhost:" + MYSERVICEUUID_UUID.toString() + ";name=" + serviceName;

    /**
     * Initialize
     */
    public void init() throws BluetoothStateException {
        localDevice = null;
        discoveryAgent = null;
        // Retrieve the local device to get to the Bluetooth Manager
        localDevice = LocalDevice.getLocalDevice();
        // Servers set the discoverable mode to GIAC
        localDevice.setDiscoverable(DiscoveryAgent.GIAC);
        // Clients retrieve the discovery agent
        discoveryAgent = localDevice.getDiscoveryAgent();
    }

    public void connect() {
        try {
            StreamConnectionNotifier streamConnectionNotifier = (StreamConnectionNotifier) Connector.open(connURL);
            StreamConnection streamConnection = streamConnectionNotifier.acceptAndOpen();
        } catch (IOException e) {
            System.err.println(Utilities.formattedTimestamp() + "Caught an Exception while trying to connect to Bluetooth Low Energy device: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void run() {
        while (true) {
            connect();
        }
    }

}
