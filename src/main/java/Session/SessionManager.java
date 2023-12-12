package Session;


import Message.Contract.AbstractContract;
import Message.Identification.AbstractIdentification;
import Message.Request.AbstractRequest;

import java.io.IOException;

public class SessionManager implements ISessionManager {

    public SessionManager() {
    }

    public <T> boolean handleSession(T object) {
        if (object instanceof AbstractIdentification) {
            IdentificationSession
        }
        else if (object instanceof AbstractRequest) {
            System.out.println(object.getClass().toString());
        }
        else if (object instanceof AbstractContract) {
            System.out.println(object.getClass().toString());
        } else {
            return false;
        }
        return true;
    }
}
