package MiscTest;

import Message.Contract.AckMessage;
import Message.MessageFlag;
import Misc.LogEntry;
import Misc.Logger;
import Misc.Utilities;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static SetupTest.TestConstant.*;


public class LoggerTest {

    @BeforeAll
    public static void testIfDirectoriesAreCreated() {
        String[] directories = {REQUEST_LOG_PATH.getTestConstant(), DELIVERY_CONTRACT_LOG_PATH.getTestConstant()};
        for (String directory : directories) {
            Logger.createLogDirectory(directory);
        }
    }

    @Test
    public void testIfAckMessageGetsSavedInRequestDirectory() throws IOException {
        LogEntry logEntry = new LogEntry(Utilities.createUUID(), Utilities.createReadableTimestamp(), null ,
                true, PEER_NAME.getTestConstant(), "Bobby");
        boolean written = Logger.writeLog(logEntry.toString(), "TestLogFile.txt");
        assertTrue(written);
    }

    @Test
    public void printOutLogEntry() {
        AckMessage ackMessage = new AckMessage(Utilities.createUUID(), MessageFlag.ACK, Utilities.createTimestamp(), true);
        LogEntry logEntry = new LogEntry(ackMessage.getUUID(), Utilities.createReadableTimestamp(), null , true, PEER_NAME.getTestConstant(), "Bobby");
        System.out.println(logEntry);
    }

    @AfterAll
    public static void clear() throws IOException {
        Files.delete(Paths.get("TestLogFile.txt" ));

    }
}
