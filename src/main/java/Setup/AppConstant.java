package Setup;

public enum AppConstant {
    // Component related constants
    APP_FORMAT("sharkHedwig"),
    PEER_FOLDER("hedwigStorage"),
    PEER_NAME("hedwig123"),
    MAX_FREIGHT_WEIGHT(3000),
    CA_ID("certificateAuthority"),
    SCHEME("sn2://"),

    // Constants for the log files. */
    REQUEST_LOG_PATH("hedwigStorage/log/request/"),
    DELIVERY_CONTRACT_LOG_PATH("hedwigStorage/log/deliveryContract/"),
    ERROR_LOG_PATH("hedwigStorage/log/errorLog.txt");

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

