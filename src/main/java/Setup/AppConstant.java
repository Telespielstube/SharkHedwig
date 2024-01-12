package Setup;

public enum AppConstant {
    // Component related constants
<<<<<<< HEAD
    AppFormat("sharkHedwig"),
    PeerFolder("HedwigStorage"),
    PeerName("hedwig123"),
    CaId("certificateAuthority"),
    Scheme("sn2://"),
=======
    APP_FORMAT("hedwig"),
    PEER_FOLDER("HedwigStorage"),
    PEER_NAME("hedwig123"),
    CA_ID("certificateAuthority"),
    SCHEME("sn2://"),
>>>>>>> tmp

    // Constants for the log files. */
    LOG_FOLDER("Log"),
    REQUEST_LOG("Request"),
    REQUEST_LOG_PATH("HedwigStorage/Log/Request/"),
    DELIVERY_CONTRACT("DeliveryContract"),
    DELIVERY_CONTRACT_LOG_PATH("HedwigStorage/Log/DeliveryContract/");

    AppConstant(String name) {}

<<<<<<< HEAD
=======
    AppConstant(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
>>>>>>> tmp
}

