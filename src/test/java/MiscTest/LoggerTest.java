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
    String[] directories = {RequestLog.getTestConstant(), DeliveryContractLogPath.getTestConstant() };
        try {
            for (String directory : directories) {
                Logger.createLogDirectory(PeerFolder.getTestConstant(), LogFolder.getTestConstant(), directory);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create logger files for request and contract sessions: " + e);
        }
    }

    @Test
    public void testIfAckMessageGetsSavedInRequestDirectory() throws IOException {
        String filepath = PeerFolder.getTestConstant() + "/" + LogFolder.getTestConstant() + "/" + RequestLog.getTestConstant();
        LogEntry logEntry = new LogEntry(Utilities.createUUID(), Utilities.createReadableTimestamp(), null ,
                true, PeerName.getTestConstant(), "Bobby");
        boolean written = Logger.writeLog(logEntry.toString(), filepath + "/" + "TestLogFile.txt");
        assertTrue(written);
    }

    @Test
    public void printOutLogEntry() {
        AckMessage ackMessage = new AckMessage(Utilities.createUUID(), MessageFlag.Ack, Utilities.createTimestamp(), true);
        LogEntry logEntry = new LogEntry(ackMessage.getUUID(), Utilities.createReadableTimestamp(), null , true, PeerName.getTestConstant(), "Bobby");
        System.out.println(logEntry);
    }

    @AfterAll
    public static void clear() throws IOException {
        String filepath = PeerFolder.getTestConstant() + "/" + LogFolder.getTestConstant() + "/" + RequestLog.getTestConstant();
        Files.delete(Paths.get(filepath + "/" + "TestLogFile.txt" ));
    }
}
