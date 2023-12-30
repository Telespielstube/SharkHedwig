package Message;

public class MessageBuilder {

    private Object message;
    private String uri;
    private String sender;

    public MessageBuilder(Object message, String uri, String sender) {
        this.message = message;
        this.uri = uri;
        this.sender = sender;
    }

    public Object getMessage() {
        return this.message;
    }

    public String getUri() {
        return this.uri;
    }

    public String getSender() {
        return this.sender;
    }

}
