package Message;

import sun.plugin2.message.Message;

public enum MessageFlag {
    Advertisement(0),
    Challenge(1),
    Response(3),
    Request(4),
    RequestReply(5),
    Contract(6),
    PickUp(7),
    Ack(8);

    private final int flag;

    MessageFlag(int flag) {
        this.flag = flag;
    }

    //Not sure if this is needed!!!!
    public int getFlag() {
        return Integer.parseInt(String.valueOf(this.flag));
    }
}
