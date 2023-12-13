package Misc;

import java.util.Date;
import java.util.UUID;

public class LogEntry {

    UUID packageId = null;
    long timestamp;
    String messageType;
    String transferor;
    String transferee;
    byte[] signatureTransferor;
    byte[] signatureTransferee;

    public LogEntry(UUID packageId, long timestamp, String transferor, String transferee, byte[] signatureTransferor, byte[] signatureTransferee) {

    }

}
