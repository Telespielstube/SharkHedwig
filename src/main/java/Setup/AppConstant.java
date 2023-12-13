package Setup;

public enum AppConstant {
    AppFormat("hedwig"),
    PeerFolder("HedwigStorage"),
    PeerName("hedwig123"),
    CaId("caID"),
    Scheme("sn2://");

    private final String name;

    AppConstant(String name) {
        this.name = name;
    }

    public String getAppConstant() {
        return this.name;
    }
}
