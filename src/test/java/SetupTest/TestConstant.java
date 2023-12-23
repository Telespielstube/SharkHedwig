package SetupTest;

public enum TestConstant {
    AppFormat("hedwigTest"),
    PeerFolder("./src/test/resources/TestStorage"),
    PeerName("hedwig123Test"),
    CaId("caID"),
    Scheme("sn2://"),
    LogFolder("LogTest"),
    RequestLog("RequestTest"),
    RequestLogPath("./src/test/resources/TestStorage/LogTest/RequestTest"),
    ContractLog("ContractTest"),
    ContractLogPath("./src/test/resources/TestStorage/LogTest/ContractTest"),
    ErrorLog("ErrorTest");
    private final String name;

    TestConstant(String name) {
        this.name = name;
    }

    public String getTestConstant() {
        return this.name;
    }
}
