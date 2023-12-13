import HedwigUI.IUserInterface;
import HedwigUI.UserInterface;
import Misc.ErrorLogger;
import Session.SessionState;
import Setup.AppConstant;
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

        sharkPeerFS = new SharkPeerFS(AppConstant.PeerName.getAppConstant(),AppConstant.PeerFolder.getAppConstant() + "/" + AppConstant.PeerName.getAppConstant() );
        new Component().setupComponent(sharkPeerFS);
        sharkPeerFS.start();
        userInterface = new UserInterface("Type \"cargo\" to enter the necessary data for the shipping label.");
        ((UserInterface) userInterface).run(); // Unfortunately, this cast is necessary because we work with interfaces.
    }
}