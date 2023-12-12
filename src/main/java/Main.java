import HedwigUI.IUserInterface;
import HedwigUI.UserInterface;
import Misc.ErrorLogger;
import Session.SessionState;
import Setup.Component;
import net.sharksystem.SharkException;

import net.sharksystem.SharkPeerFS;

import java.io.IOException;

import static Misc.Constants.*;

public class Main {
    /**
     * This is an application to provide mobile drones with the ability to exchange physical packages in a secure way.
     * The goal is to provide a fully working protocol written in Java 8.
     *
     * @param args                  Empty arguments array passed from the cli.
     * @throws SharkException       If the setup process of a shark component fails this exception is thrown.
     * @throws IOException          If something fails during the channel setup this exception is thrown.
     *
     */
    public static void main(String[] args) throws SharkException, IOException {
        ErrorLogger.redirectErrorStream();
        SharkPeerFS sharkPeerFS;
        IUserInterface userInterface;
        Component component = new Component();
        System.err.print("This goes to file");
        if (Misc.Utilities.checkDroneID()) {
            sharkPeerFS = new SharkPeerFS(PEER_NAME, PEER_FOLDER + "/" + PEER_NAME);
            component.setupComponent(sharkPeerFS);
            sharkPeerFS.start();
            SessionState.NoState.getState();
            userInterface = new UserInterface("Type \"Package\" to enter the necessary data for the shipping label.");
            ((UserInterface) userInterface).run(); // Unfortunately, this cast is necessary because we work with interfaces.
        } else {
            System.err.println("Drone ID incorrect. Component could not be setup.");
            System.exit(0);
        }
    }
}