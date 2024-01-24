package Setup;

public enum AppConstant {
    // Component related constants
    APP_FORMAT("SharkHedwig"),
    PEER_FOLDER("HedwigStorage"),
    PEER_NAME("hedwig123"),
    MAX_FREIGHT_WEIGHT(3000),
    CA_ID("certificateAuthority"),
    SCHEME("sn2://"),

    // Constants for the log files. */
    REQUEST_LOG_PATH("HedwigStorage/Log/Request/"),
    DELIVERY_CONTRACT_LOG_PATH("HedwigStorage/Log/DeliveryContract/"),
    ERROR_LOG_PATH("HedwigStorage/Log/ErrorLog.txt");

    private String name;
    private int grams;

    AppConstant(String name) {
        this.name = name;
    }

    AppConstant(int grams) {
        this.grams = grams;
    }
    public String toString() {
        return name;
    }

    public int getInt() {
        return grams;
    }
}

