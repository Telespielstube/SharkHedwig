package DeliveryContract;

import Location.Location;
import java.util.UUID;

public class TransitEntry {

    private int serialNumber = 0;
    private UUID packageUUID;
    private String transferor;
    private String transferee;
    private Location handoverLocation;
    private long timestamp;
    private byte[] digitalSignature;

    public TransitEntry(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * Constructor to write the transit record entry.
     *
     * @param serialNumber           Counting number for every entry.
     * @param packageUUID            Package UUID to identify the package.
     * @param transferor             Sender of the package.
     * @param transferee             Recipient of the package.
     * @param timestamp              Timestamp of the package handover.
     * @param digitalSignature       digitalSignature of the newly adeed entry.
     */
    public TransitEntry(int serialNumber, UUID packageUUID, String transferor, String transferee, Location handoverLocation,
                        long timestamp, byte[] digitalSignature) {
        this.serialNumber = serialNumber;
        countUp();
        this.packageUUID = packageUUID;
        this.transferor = transferor;
        this.transferee = transferee;
        this.handoverLocation = handoverLocation;
        this.timestamp = timestamp;
        this.digitalSignature = digitalSignature;
    }

    /**
     * Just adds 1 tho the serialNumber.
     *
     * @return    serial number plus 1.
     */
    public int countUp() {
        return this.serialNumber++;
    }

    /**
     * Serial number of the entry.
     *
     * @return    Serial number.
     */
    public int getSerialNumber() {
        return this.serialNumber;
    }

    /**
     * Sets the transferee attribute to complete the TransitEntry object.
     *
     * @param transferee    The expectant receiver of the package.
     */
    public void setTransferee(String transferee) {
        this.transferee = transferee;
    }

    public String getTransferee() {
        return this.transferee;
    }

    public Location getHandoverLocation() {
        return this.handoverLocation;
    }

    public void setDigitalSignature(byte[] digitalSignature) {
        this.digitalSignature = digitalSignature;
    }
    /**
     * Writes all attributes of the Entry object as String representation.
     *
     * @return    String object of  all class attributes.
     */
    public String toString() {
        return String.format(this.serialNumber + " " + this.packageUUID + " " + this.transferor + " " + this.transferee
                + " " + this.handoverLocation + " " + this.timestamp + " " + this.digitalSignature);
    }
}
