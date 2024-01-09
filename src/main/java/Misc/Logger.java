package Misc;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

/**
 * Saves communication sessions to disk to make them accessable for the future.
 * The IO operations make use of the Java.nio.files package.
 */
public class Logger {

    /**
     * Creates the logging folder for request and contract sessions.
     *
     * @param peerFolder Name of the Application folder.
     * @param logFolder  Name of the log folder.
     * @throws IOException is thrown when something is wrong in creating the folder.
     */
    public static void createLogDirectory(String peerFolder, String logFolder, String directory) throws IOException {
        Files.createDirectories(Paths.get(peerFolder + "/" + logFolder + "/" + directory));
    }

    /**
     * Opens a stream to write the LogEntry object as String to the log file. The methode makes use of the Java.nio.file
     *
     * @param entry LogEntry object.
     * @param file  File where the LogEntry should be written to.
     * @return true if writing was successful.
     */
    public static boolean writeLog(String entry, String file) {
        Path path = Paths.get(file);
        try {
            Files.write(path, entry.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return true;
    }
}