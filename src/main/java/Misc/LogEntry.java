package Misc;

import Location.Location;
import java.util.UUID;

public class LogEntry {
    private UUID packageUUID;
    private String timestamp;
    private Location pickup;
    private boolean accepted;
    private String transferor;
    private String transferee;

    /**
     * Constructor for the request session log.
     * @param packageUUID     Universally Unique Identifier for the package.
     * @param timestamp       current timestamp
     * @param transferor      Package holder.
     * @param transferee      Peer with no carriage.
     * @param accepted       set if the request session was successful.
     */
    public LogEntry(UUID packageUUID, String timestamp, Location pickup, boolean accepted, String transferor, String transferee) {
        this.packageUUID = packageUUID;
        this.timestamp = timestamp;
        this.pickup = pickup;
        this.accepted = accepted;
        this.transferor = transferor;
        this.transferee = transferee;
    }


    @Override
    public String toString() {
        return ("PackageUUID :" + this.packageUUID + "; Message created: " + this.timestamp + "; Handover: " + this.pickup
                + "; Accepted: " + this.accepted + "; Transferor: " + this.transferor + "; Transferee: " + this.transferee);
    }
}
