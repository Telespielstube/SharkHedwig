package Setup;

import User.UserManager;

/**
 * The HttpServer class sets up an object that allow the component to receive the shipping label data from a
 * device connected to the same network. This class does not serve as a server
 * for P2P connections between two SharkHedwigComponents.
 */
public class HttpServer implements Runnable {

    private final UserManager userManager;

    public HttpServer(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public void run() {

    }

}
