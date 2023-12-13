package Session;

import HedwigUI.DeviceState;
import Message.IMessageHandler;

public interface ISessionManager {

    /**
     * Checks the passed object in which session it fits.
     * @param object
     * @param <T>
     */
    <T> boolean handleSession(T object, String sender, IMessageHandler messageHandler, SessionState sessionState, DeviceState deviceState);

}
