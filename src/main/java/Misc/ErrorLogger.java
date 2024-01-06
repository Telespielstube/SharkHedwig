package Misc;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


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
        String path = peerFolder + "/" + logFolder + "/" + errorLog;
        try {
            Files.createDirectories(Paths.get(peerFolder + "/" + logFolder));
            Files.createFile(Paths.get(path));
            System.setErr(new PrintStream(new FileOutputStream(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
