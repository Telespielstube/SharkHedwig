package DeliveryContract;

import Location.Location;

import java.util.Arrays;
import java.util.UUID;

public class TransitEntry {

    private int serialNumber = 0;
    private UUID packageUUID;
    private String transferor;
    private String transferee;
    private Location pickUpLocation;
    private long timestamp;
    private byte[] signatureTransferee;
    private byte[] signatureTransferor;

    /**
     * Constructor to write the transit record entry.
     *
     * @param serialNumber           Counting number for every entry.
     * @param packageUUID            Package UUID to identify the package.
     * @param transferor             Sender of the package.
     * @param transferee             Recipient of the package.
     * @param timestamp              Timestamp of the package handover.
     * @param signatureTransferee    Transferee signature.
     * @param signatureTransferor    Transferor signature.
     */
    public TransitEntry(int serialNumber, UUID packageUUID, String transferor, String transferee, Location handoverLocation,
                        long timestamp, byte[] signatureTransferee, byte[] signatureTransferor) {
        this.serialNumber = serialNumber;
        this.packageUUID = packageUUID;
        this.transferor = transferor;
        this.transferee = transferee;
        this.pickUpLocation = handoverLocation;
        this.timestamp = timestamp;
        this.signatureTransferee = signatureTransferee;
        this.signatureTransferor = signatureTransferor;
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

    public Location getPickUpLocation() {
        return this.pickUpLocation;
    }

    public byte[] getSignatureTransferee() {
        return this.signatureTransferee;
    }

    public void setSignatureTransferee(byte[] signatureTransferee) {
        this.signatureTransferee = signatureTransferee;
    }

    public byte[] getSignatureTransferor() {
        return this.signatureTransferor;
    }

    public void setSignatureTransferor(byte[] signatureTransferor) {
        this.signatureTransferor = signatureTransferor;
    }

    /**
     * Writes all attributes of the Entry object as String representation.
     *
     * @return    String object of  all attributes.
     */
    public String toString() {
        return String.format("S/N: " + this.serialNumber + "; PackageUUID: " + this.packageUUID + "; Transferor: " +
                this.transferor + "; Transferee: " + this.transferee + "; Pick up Location: " +
                this.pickUpLocation + "; Timestamp: " + this.timestamp + "; Signature Transferee: " +
                Arrays.toString(this.signatureTransferee) + "; Signature Transferor: " +
                Arrays.toString(this.signatureTransferor) + "\n");
    }
}
