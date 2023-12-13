package Session;

import HedwigUI.DeviceState;

public interface ISessionManager {

    /**
     * Checks the passed object in which session it fits.
     * @param object
     * @param <T>
     */
    <T> boolean handleSession(T object, String sender, SessionState sessionState, DeviceState deviceState);

}
