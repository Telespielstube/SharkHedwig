package Misc;

import java.io.Serializable;
import Location.Location;
import java.util.UUID;

public class LogEntry implements Serializable {

    private UUID packageUUID;
    private long timestamp;
    private Location handover;
    private boolean concluded;
    private String messageType;
    private String transferor;
    private String transferee;
    private byte[] signatureTransferor;
    private byte[] signatureTransferee;

    public LogEntry() {}

    public LogEntry(UUID packageUUID, long timestamp, boolean concluded, String messageType, String transferor, String transferee, byte[] signatureTransferor, byte[] signatureTransferee) {
        this.packageUUID = packageUUID;
        this.timestamp = timestamp;
        this.concluded = concluded;
        this.messageType = messageType;
        this.transferor = transferor;
        this.transferee = transferee;
    }
    public LogEntry(UUID packageUUID, long timestamp, Location handover, String messageType, String transferor, String transferee, byte[] signatureTransferor, byte[] signatureTransferee) {
        this.packageUUID = packageUUID;
        this.timestamp = timestamp;
        this.handover = handover;
        this.messageType = messageType;
        this.transferor = transferor;
        this.transferee = transferee;
    }

}
