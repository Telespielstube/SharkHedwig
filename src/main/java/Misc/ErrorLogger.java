package Misc;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;


/**
 * This class has one purpose only. It readirects the standard error stream to a file instead of the cli. This makes
 * runtime errors persistent and more accessible and healp in finding bugs in the source code.
 */
public class ErrorLogger {

    /**
     * Redirects the error stream to the passed parameters.
     *
     * @param peerFolder    Application root folder.
     * @param logFolder     Folder for all log files.
     * @param errorLog      Error log file name.
     *
     */
    public static void redirectErrorStream(String peerFolder, String logFolder, String errorLog) {
        try {
            System.setErr(new PrintStream(new FileOutputStream("./" + peerFolder + "/" + logFolder + "/" + errorLog)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
