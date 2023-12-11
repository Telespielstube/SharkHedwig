package DeliveryContract;

import Location.Location;
import Misc.Utilities;

import java.security.Signature;
import java.util.UUID;

public class TransitEntry {

    private UUID packageUUID;
    private int serialNumber;
    private String transferor;
    private String transferee;
    private Location handoverLocation;
    private long timestamp;
    private Signature transferorSignature;
    private Signature transfereeSignature;

    public TransitEntry() {}

    /**
     * Constructor to write the transit record entry.
     *
     * @param serialNumber           Counting number for eveery entry.
     * @param packageUUID            Package UUID to ifentifiy the package.
     * @param transferor             Sender of the package.
     * @param transferee             Recipient of the package.
     * @param timestamp              Timestamp of the package handover.
     * @param transferorSignature    Signature of the sender.
     * @param transfereeSignature    Signature of the recipient.
     */
    public TransitEntry(int serialNumber, UUID packageUUID, String transferor, String transferee, Location handoverLocation,
                        String timestamp, Signature transferorSignature, Signature transfereeSignature) {
        this.serialNumber = serialNumber;
        this.packageUUID = packageUUID;
        this.transferee = transferee;
        this.handoverLocation = handoverLocation;
        this.timestamp = Utilities.getTimestamp();
        this.transferorSignature = transferorSignature;
        this.transfereeSignature = transfereeSignature;
    }

    /**
     * Writes all attributes of the Entry object as String representation.
     *
     * @return    String object of  all class attributes.
     */
    public String toString() {
        return String.format(this.serialNumber + " " + this.packageUUID + " " + this.transferor + " " + this.transferee
                + " " + this.handoverLocation + " " + this.timestamp + " " + this.transferorSignature + " " + this.transfereeSignature);
    }
}
