package Misc;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.Date;

import static Misc.Constants.*;
import static Misc.Constants.LOG_FOLDER;

/**
 * Saves communication sessions to disk to make them accessable for the future.
 *
 * @param <T>   Generic to pass either a RequestLog or a ContragLog object.
 */
public class SessionLogger<T> {
    UUID logID = null;
    Date timestamp = null;
    long transferorID = 0;
    long transfereeID = 0;
    String messageType = "";
    byte[] signatureTransferor = null;
    byte[] signatureTransferee = null;

    /** Creates the logging folder for request and contract sessions.
     *
     * @param directory        describes the name of the folder.
     * @throws IOException      is thrown when something is wrong in creating the folder.
     *
     */
    public static void createLogDirectory() throws IOException {
        Files.createDirectories(Paths.get(PEER_FOLDER + "/" + LOG_FOLDER));
    }

    /**
     * Creates the logging file.
     *
     * @param logFile           the logging file where the entries are written to.
     * @throws IOException      thrown if something went wrong while creating the log file.
     *
     */
    public static void createLoggerFile(String logFile) throws IOException {
        File file = new File("./" + PEER_FOLDER + "/" + LOG_FOLDER + "/" + logFile);
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
    public boolean writeEntry(LogEntry entry, String file) {
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