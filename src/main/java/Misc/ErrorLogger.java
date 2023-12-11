package Misc;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import static Misc.Constants.LOG_FOLDER;
import static Misc.Constants.PEER_FOLDER;

/**
 * This class has only one purpose. It readirects the standard error stream to a file instead of the cli. This makes
 * runtime errors persistent and more accessible and healp in finding bugs in the source code.
 */
public class ErrorLogger {

    public static void redirectErrorStream() {
        try {
            System.setErr(new PrintStream(new FileOutputStream("./" + PEER_FOLDER + "/" + LOG_FOLDER + "/" + "errorlog.txt")));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}