package SetupTest;

import java.nio.file.Path;

public enum TestConstant {
    AppFormat("hedwigTest"),
    PeerFolder("./src/test/resources/TestStorage"),
    PeerName("hedwig123Test"),
    CaId("caID"),
    Scheme("sn2://"),
    LogFolder("LogTest"),
    RequestLog("RequestTest"),
    DeliveryContractLogPath("DeliveryContractTest"),
    ErrorLog("ErrorTest");
    private final String name;

    TestConstant(String name) {
        this.name = name;
    }

    public String getTestConstant() {
        return this.name;
    }
}
