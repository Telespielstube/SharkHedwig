package Message;

import sun.plugin2.message.Message;

public enum MessageFlag {
    Advertisement(0),
    Challenge(1),
    Response(3),
    ResponseRelpy(4),
    Request(5),
    RequestReply(6),
    Contract(7),
    Confirmation(8),
    PickUp(9),
    Ack(10);

    private final int flag;

    MessageFlag(int flag) {
        this.flag = flag;
    }

    //Not sure if this is needed!!!!
    public int getFlag() {
        return Integer.parseInt(String.valueOf(this.flag));
    }
}
