package Setup;

public enum AppConstant {
    // Component related constants
    APP_FORMAT("hedwig"),
    PEER_FOLDER("HedwigStorage"),
    PEER_NAME("hedwig123"),
    CA_ID("certificateAuthority"),
    SCHEME("sn2://"),

    // Constants for the log files. */
    LOG_FOLDER("Log"),
    REQUEST_LOG("Request"),
    REQUEST_LOG_PATH("HedwigStorage/Log/Request/"),
    DELIVERY_CONTRACT("DeliveryContract"),
    DELIVERY_CONTRACT_LOG_PATH("HedwigStorage/Log/DeliveryContract/");

    private final String name;

    AppConstant(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
