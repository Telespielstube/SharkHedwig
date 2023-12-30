package Misc;

import java.io.Serializable;
import Location.Location;
import java.util.UUID;

public class LogEntry implements Serializable {

    private UUID packageUUID;
    private long timestamp;
    private Location handover;
    private boolean concluded;
    private String transferor;
    private String transferee;

    public LogEntry() {}

    /**
     * Constructor for the request session log.
     * @param packageUUID     Universally Unique Identifier for the package.
     * @param timestamp       current timestamp
     * @param transferor      Package holder.
     * @param transferee
     * @param concluded       set if the request session was successful.
     */
    public LogEntry(UUID packageUUID, long timestamp, boolean concluded, String transferor, String transferee) {
        this.packageUUID = packageUUID;
        this.timestamp = timestamp;
        this.concluded = concluded;
        this.transferor = transferor;
        this.transferee = transferee;
    }

    /**
     * Constructor for the request session log.
     * @param packageUUID     Universally Unique Identifier for the package.
     * @param timestamp       current timestamp.
     * @param handover        Location where the handover takes place.
     * @param transferor      Package holder.
     * @param transferee

     */
    public LogEntry(UUID packageUUID, long timestamp, Location handover, boolean concluded, String transferor, String transferee) {
        this.packageUUID = packageUUID;
        this.timestamp = timestamp;
        this.concluded = concluded;
        this.handover = handover;
        this.transferor = transferor;
        this.transferee = transferee;
    }

    @Override
    public String toString() {
        return ("PackageUUID :" + this.packageUUID + " Message created: " + this.timestamp + " Handover: " + this.handover
                + "Accepted: " + this.concluded + " Transferor: " + this.transferor + " Transferee: " + this.transferee);
    }
}
