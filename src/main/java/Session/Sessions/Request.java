package Session.Sessions;

import Session.ISessionManager;

public class Request  implements ISession {
    @Override
    public Object unpackMessage(Object message) {
        return null;
    }

    @Override
    public boolean compareTimestamp(long timestamp) {
        return false;
    }

    @Override
    public boolean isSessionComplete() {
        return false;
    }
}
