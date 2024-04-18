package Message;

public enum MessageFlag {
    ADVERTISEMENT(0),
    SOLICITATION(1),
    OFFER(2),
    OFFER_REPLY(3),
    CONFIRM(4),
    CONTRACT_DOCUMENT(5),
    AFFIRM(6),
    PICK_UP(7),
    READY_TO_PICK_UP(8),
    RELEASE(9),
    COMPLETE(10);

    private final int flag;

    MessageFlag(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return this.flag;
    }
}
