package Message;

public enum MessageFlag {
    ADVERTISEMENT(0),
    OFFER(4),
    OFFER_REPLY(5),
    CONFIRM(6),
    CONTRACT_DOCUMENT(7),
    PICK_UP(8),
    ACK(9),
    READY(10),
    COMPLETE(11);

    private final int flag;

    MessageFlag(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return Integer.parseInt(String.valueOf(this.flag));
    }
}
