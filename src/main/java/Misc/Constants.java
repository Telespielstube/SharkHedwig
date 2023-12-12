package Misc;

/**
 * All contants used in the application are going in here.
 */
public final class Constants {

    /** App relevant constants */
    public static final String PEER_FOLDER = "HedwigStorage";
    public static final String APP_FORMAT = "hedwig";
    public static final String CA_ID = "caID";
    public static final String PEER_NAME = "Hedwig123";
    public static final String SCHEME ="sn2://";

    /** User Interface relevant*/
    public static final String PACKAGE = "package";

    /** Constants for the log files. */
    public static final String LOG_FOLDER = "Logger";
    public static final String REQUEST_LOGFILE = "Requests";
    public static final String CONTRACT_LOGFILE = "Contracts";

    /** Relevant location constants. */
    public static final double PARALLEL_OF_LATITUDE = 111.3;
    public static final double PARALLEL_OF_LONGITUDE = 71.5;

    /** Flags for setting the session state */
    public static final int NO_SESSION = 0;
    public static final int PING_SESSION = 1;
    public static final int IDENTIFICATION_SESSION = 2;
    public static final int REQUEST_SESSION = 3;
    public static final int CONTRACT_SESSION = 4;

    /** Flags for setting the message type */
    public static final int PING_MESSAGE_FLAG = 5;
    public static final int CHALLENGE_MESSAGE_FLAG = 6;
    public static final int RESPONSE_MESSAGE_FLAG = 7;
    public static final int REQUEST_MESSAGE_FLAG = 8;
    public static final int REQUEST_REPLY_MESSAGE_FLAG = 8;
    public static final int CONTRACT_MESSAGE_FLAG = 10;
    public static final int PICK_UP_MESSAGE_FLAG = 11;
    public static final int ACK_MESSAGE_FLAG = 12;
}
