package Message;

public enum MessageFlag {
    Advertisement(0),
    Challenge(1),
    Response(3),
    ResponseReply(4),
    Offer(5),
    OfferReply(6),
    Confirm(7),
    Contract(8),
    Confirmation(9),
    PickUp(10),
    Ack(11),
    Ready(12);

    private final int flag;

    MessageFlag(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return Integer.parseInt(String.valueOf(this.flag));
    }
}
