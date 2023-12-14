package Misc;

import Setup.Constant;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Saves communication sessions to disk to make them accessable for the future.
 *
 */
public class SessionLogger {


    /** Creates the logging folder for request and contract sessions.
     *
     * @param peerFolder        Name of the Application folder.
     * @param logFolder         Name of the log folder.
     * @throws IOException      is thrown when something is wrong in creating the folder.
     *
     */
    public static void createLogDirectory(String peerFolder, String logFolder) throws IOException {
        Files.createDirectories(Paths.get(peerFolder + "/" + logFolder));
    }

    /**
     * Creates the logging file.
     *
     * @param logFile           the logging file where the entries are written to.
     * @throws IOException      thrown if something went wrong while creating the log file.
     *
     */
    public static void createLogFile(String peerFolder, String logFolder, String logFile) throws IOException {
        File file = new File("./" + peerFolder + "/" + logFolder + "/" + logFile);
        if (file.createNewFile()) {
            System.out.println(logFile + " log file is created.");
        } else {
            System.out.println(logFile + " log file alreadcy exits.");
        }
    }

    /**
     * Opens a stream to write the LogEntry object to the log file.
     *
     * @param entry             LogEntry object.
     * @param file              File where the LogEntry should be written to.
     * @return                  true if writing was successful.
     * @throws IOException
     */
    public static boolean writeEntry(LogEntry entry, String file) {
        try (FileOutputStream openWriteStream = new FileOutputStream(file)) {
            ObjectOutputStream writeLogEntry = new ObjectOutputStream(openWriteStream);
            writeLogEntry.writeObject(entry);
            writeLogEntry.flush();
            openWriteStream.flush();
        } catch (IOException e) {
            System.err.println("Log entry could not be written to " + file + ". Please check the error log file." + e);
        }
        return true;
    }
}