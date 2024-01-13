package Misc;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Saves communication sessions to disk to make them accessable for the future.
 * The IO operations make use of the Java.nio.files package.
 */
public class Logger {

    /**
     * Creates the logging folder for request and contract sessions.
     *
     * @param directory    Path to log directory,
     * @throws IOException is thrown when something is wrong in creating the folder.
     */
    public static void createLogDirectory(String directory) {
        try {
            Files.createDirectories(Paths.get(directory));
        } catch (IOException e) {
            System.err.println("Caught an Exception while creating the log folders: " + e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Opens a stream to write the LogEntry object as String to the log file. The methode makes use of the Java.nio.file
     *
     * @param entry LogEntry object.
     * @param file  File where the LogEntry should be written to.
     * @return true if writing was successful.
     */
    public static boolean writeLog(String entry, String file) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(file), StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
            writer.write(entry);
        } catch (IOException e) {
            System.err.println("Caught an exception while writing log data: " + e);
            throw new RuntimeException(e);
        }
        return true;
    }
}