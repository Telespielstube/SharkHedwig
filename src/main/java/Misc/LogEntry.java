package Misc;

import DeliveryContract.DeliveryContract;
import Location.Location;
import java.util.UUID;

public class LogEntry {
    private double packageWeight;
    private String timestamp;
    private Location packageDestination;
    private boolean accepted;
    private String transferor;
    private String transferee;
    private DeliveryContract deliveryContract;

    /**
     * Constructor for the request session log.
     *
     * @param transferee            Peer with no carriage.
     * @param transferor            Package holder.
     * @param timestamp             current timestamp.
     * @param packageWeight         Actual weight of the package.
     * @param packageDestination    Final destination of the delivery.
     * @param accepted              set if the request session was successful.
     *
     */
    public LogEntry(String transferor, String transferee, String timestamp, double packageWeight, Location packageDestination, boolean accepted) {
        this.transferor = transferor;
        this.transferee = transferee;
        this.timestamp = timestamp;
        this.packageWeight = packageWeight;
        this.packageDestination = packageDestination;
        this.accepted = accepted;

    }

    /**
     * Constructor for the contract session.
     *
     * @param deliveryContract    DeliveryContract object.
     */
    public LogEntry(DeliveryContract deliveryContract) {
        this.deliveryContract = deliveryContract;
    }

    /**
     * String representation of the relevant request data to write to disk.
     *
     * @return    String object of the relevant request data.
     */
    public String getRequestLogEntry() {
        return String.format("Request\n-------\n\nSender: " + this.transferor + ";\nReceiver: " + this.transferee + ";\nMessage created: " + this.timestamp + ";\n" +
                "Package weight: " + this.packageWeight +";\nDestination: " + this.packageDestination + ";\n\n");
    }

    /**
     * String representation of the relevant contract data to write to disk.
     *
     * @return    String object of the relevant contract data.
     */
    public String getDeliveryContractLogEntry() {
        return String.format("DeliveryContract\n-----------------\n\n" + "Shipping label\n--------------\n" + this.deliveryContract.getShippingLabel().toString() +
                "\n\n" + "Transit record\n--------------\n" + this.deliveryContract.getTransitRecord().toString());
    }
}
