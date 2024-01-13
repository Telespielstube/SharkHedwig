package SetupTest;

public enum TestConstant {
    APP_FORMAT("hedwigTest"),
    PEER_FOLDER("./src/test/resources/TestStorage"),
    PEER_NAME("hedwig123Test"),
    CA_ID("caID"),
    SCHEME("sn2://"),
    LOG_FOLDER("LogTest"),
    REQUEST_LOG_PATH("./src/test/resources/TestStorage/Log/RequestTest"),
    DELIVERY_CONTRACT_LOG_PATH("./src/test/resources/TestStorage/LogTest/DeliveryContractTest"),
    ERROR_LOG_PATH("./src/test/resources/TestStorage/Log/ErrorTest");

    private final String name;

    TestConstant(String name) {
        this.name = name;
    }

    public String getTestConstant() {
        return this.name;
    }
}
