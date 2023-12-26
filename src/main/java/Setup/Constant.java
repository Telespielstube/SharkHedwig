package Setup;

public enum Constant {
    // Component related constants
    AppFormat("hedwig"),
    PeerFolder("HedwigStorage"),
    PeerName("hedwig123"),
    CaId("ceretificateAuthority"),
    Scheme("sn2://"),

    // Constants for the log files. */
    LogFolder("Log"),
    RequestLog("Request"),
    RequestLogPath("HedwigStroage/Log/Request.txt"),
    ContractLog("Contract"),
    ContractLogPath("HedwigStorage/Log/Contract.txt");
    private final String name;

    Constant(String name) {
        this.name = name;
    }

    public String getAppConstant() {
        return this.name;
    }
}
