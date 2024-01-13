package Setup;

public enum AppConstant {
    // Component related constants
    APP_FORMAT("SharkHedwig"),
    PEER_FOLDER("HedwigStorage"),
    PEER_NAME("hedwig123"),
    CA_ID("certificateAuthority"),
    SCHEME("sn2://"),

    // Constants for the log files. */
    REQUEST_LOG_PATH("HedwigStorage/Log/Request/"),
    DELIVERY_CONTRACT_LOG_PATH("HedwigStorage/Log/DeliveryContract/"),
    ERROR_LOG_PATH("HedwigStorage/Log/ErrorLog.txt");

    private String name;

    AppConstant(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}

