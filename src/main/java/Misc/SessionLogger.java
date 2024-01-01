package Misc;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Saves communication sessions to disk to make them accessable for the future.
 * The IO operations make use of the Java.nio.files package.
 */
public class SessionLogger {

    /**
     * Creates the logging folder for request and contract sessions.
     *
     * @param peerFolder Name of the Application folder.
     * @param logFolder  Name of the log folder.
     * @throws IOException is thrown when something is wrong in creating the folder.
     */
    public static void createLogDirectory(String peerFolder, String logFolder) throws IOException {
        Files.createDirectories(Paths.get(peerFolder + "/" + logFolder));
    }

    /**
     * Creates the logging file. Makes no use of nio because it could be that the file already exists.
     *
     * @param logFile the logging file where the entries are written to.
     * @throws IOException thrown if something went wrong while creating the log file.
     */
    public static void createLogFile(String peerFolder, String logFolder, String logFile) throws IOException {
        Path filePath = Paths.get("./" + peerFolder + "/" + logFolder + "/" + logFile);
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
            System.out.println(logFile + " log file is created.");
        } else {
            System.out.println(logFile + " log file alreadcy exits.");
        }
    }

    /**
     * Opens a stream to write the LogEntry object as String to the log file. The methode makes use of the Java.nio.file
     *
     * @param entry LogEntry object.
     * @param file  File where the LogEntry should be written to.
     * @return true if writing was successful.
     * @throws IOException
     */
    public static boolean writeEntry(String entry, String file) {
        try {
            Files.write(Paths.get(file), entry.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}