package Session;

import Message.IMessage;
import Message.MessageBuilder;
import Setup.DeviceState;
import net.sharksystem.pki.SharkPKIComponent;

public interface ISessionManager {

    /**
     * Checks if the current device state is transferor or transferee.
     *
     * @return    True if state is Tranferor else false;
     */
    boolean checkTransferorState();

    /**
     * The incming messages are handled differently based on the current device state.
     * This methode handles the transferor role.
     *
     * @param message    Incomming generic type Message object.
     */
     MessageBuilder sessionHandling(IMessage message, String sender);

    public void resetAll();

//    /**
//     * Methode to convert the Message object to an encrypted signed byte[] and send it as an ASAPMessage.
//     */
//    void handleOutgoing(Object messageObject, String uri, String sender, SharkPKIComponent sharkPKIComponent);
}
