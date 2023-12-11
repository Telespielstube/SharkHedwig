package Session;


public class SessionManager {

    private T object;

    public <T> SessionManager(T object) {
        this.object = object;
    }
}
