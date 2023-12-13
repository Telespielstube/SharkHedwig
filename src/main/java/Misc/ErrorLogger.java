package Misc;

import Setup.AppConstant;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import static Misc.Constants.LOG_FOLDER;

/**
 * This class has only one purpose. It readirects the standard error stream to a file instead of the cli. This makes
 * runtime errors persistent and more accessible and healp in finding bugs in the source code.
 */
public class ErrorLogger {

    public static void redirectErrorStream() {
        String errorLog = "errorLog.txt";
        try {
            System.setErr(new PrintStream(new FileOutputStream("./" + AppConstant.PeerFolder.getAppConstant() + "/" + AppConstant.LogFolder + "/" + errorLog)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
