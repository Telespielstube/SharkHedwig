package Message;

public enum MessageFlag {
    ADVERTISEMENT(0),
    SOLICITATION(1),
    OFFER(2),
    OFFER_REPLY(3),
    CONFIRM(4),
    CONTRACT_DOCUMENT(5),
    PICK_UP(6),
    ACK(7),
    READY(8),
    COMPLETE(9);

    private final int flag;

    MessageFlag(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return Integer.parseInt(String.valueOf(this.flag));
    }
}
