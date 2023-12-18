package Session;

import Message.IMessage;
import Setup.DeviceState;

public interface ISessionManager {



    /**
     * Checks if the current device state is transferor or transferee and passes the message to the right methode.
     *
     * @param
     */
    boolean checkTransferorState();

    /**
     * The incming messages are handled differently based on the current device state.
     * This methode handles the transferor role.
     *
     * @param message    Incomming generic type Message object.
     */
     void sessionHandling(IMessage message, String sender);

    /**
     * The incoming messages are handled differently based on the current device state.
     * This methode handles the transferee role.
     *
     * @param message    Incoming generic type Message object.
     */
    <T> void handleTransferee(IMessage message, String sender);

    /**
     * Methode to convert the Message object to an encrypted signed byte[] and send it as an ASAPMessage.
     */
    void handleOutgoing();
}
