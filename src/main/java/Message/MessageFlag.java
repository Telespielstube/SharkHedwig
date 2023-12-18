package Message;

public enum MessageFlag {
    Advertisement(0),
    Challenge(1),
    Response(3),
    ResponseReply(4),
    Offer(5),
    OfferReply(6),
    Contract(7),
    Confirmation(8),
    PickUp(9),
    Ack(10);

    private final int flag;

    MessageFlag(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return Integer.parseInt(String.valueOf(this.flag));
    }
}
