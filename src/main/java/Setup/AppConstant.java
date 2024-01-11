package Setup;

public enum AppConstant {
    // Component related constants
    AppFormat("hedwig"),
    PeerFolder("HedwigStorage"),
    PeerName("hedwig123"),
    CaId("certificateAuthority"),
    Scheme("sn2://"),

    // Constants for the log files. */
    LogFolder("Log"),
    RequestLog("Request"),
    RequestLogPath("HedwigStorage/Log/Request/"),
    DeliveryContract("DeliveryContract"),
    DeliveryContractLogPath("HedwigStorage/Log/DeliveryContract/");

    private final String name;

    AppConstant(String name) {
        this.name = name;
    }
}
