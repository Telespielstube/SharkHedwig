package Channel;

public class Channel {

    private final String SCHEME ="sn2://";

    public Channel() {}

    public String getChannelSchema() {
        return SCHEME;
    }

    public String getChannelTypeKeyExchange() {
        return SCHEME + Type.IDENTIFICATION;
    }

    public String getChannelTypeProposal() {
        return SCHEME + Type.REQUEST;
    }

    public String getChannelTypeHandover() {
        return SCHEME + Type.HANDOVER;
    }



}
