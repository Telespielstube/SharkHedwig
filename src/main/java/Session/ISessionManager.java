package Session;

import Setup.DeviceState;

public interface ISessionManager {

    /**
     * Checks the passed object in which session it fits.
     * @param object
     * @param <T>
     */
    <T> boolean handleSession(T object, String sender, SessionState sessionState, DeviceState deviceState);

    /**
     * The incming messages are handled differently based on the current device state.
     * This methode handles the transferor role.
     *
     * @param message    Incomming generic type Message object.
     */
    void transferorMessage(T message);

    /**
     * The incming messages are handled differently based on the current device state.
     * This methode handles the transferee role.
     *
     * @param message    Incomming generic type Message object.
     */
    void transfereeMessage(T message);
}
