import HedwigUI.IUserInterface;
import HedwigUI.UserInterface;
import Misc.ErrorLogger;
import Setup.Constant;
import Setup.Component;
import net.sharksystem.SharkException;

import net.sharksystem.SharkPeerFS;

import java.io.IOException;

public class Main {
    /**
     * This is an application to provide mobile drones with the ability to exchange physical packages in a secure way.
     * The protocol is written in Java 8.
     *
     * @Author Martina Brüning [s0540636] HTW-Berlin
     *
     * @param args                  Empty arguments array passed from the cli.
     *
     */
    public static void main(String[] args) {
        SharkPeerFS sharkPeerFS;
        ErrorLogger.redirectErrorStream(Constant.PeerFolder.getAppConstant(), Constant.LogFolder.getAppConstant(), "errorLog.txt");
        sharkPeerFS = new SharkPeerFS(Constant.PeerName.getAppConstant(), Constant.PeerFolder.getAppConstant() + "/" + Constant.PeerName.getAppConstant() );
        try {
            new Component().setupComponent(sharkPeerFS);
            sharkPeerFS.start();
        } catch (SharkException e) {
            throw new RuntimeException(e);
        }

        IUserInterface userInterface = new UserInterface("Type \"cargo\" to enter the necessary data for the shipping label.");
        ((UserInterface) userInterface).run(); // Unfortunately, this cast is necessary because we work with interfaces.
    }
}