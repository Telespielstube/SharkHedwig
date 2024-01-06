package MiscTest;

import Message.Contract.AckMessage;
import Message.MessageFlag;
import Misc.*;
import SetupTest.TestConstant;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static SetupTest.TestConstant.*;


public class SessionLoggerTest {


    @BeforeAll
    public static void testIfDirectoryAndFileAreCreated() {
        try {
            SessionLogger.createLogDirectory(PeerFolder.getTestConstant(), LogFolder.getTestConstant());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testIfAckMessageGetsSavedInRequestFile() throws IOException {
        SessionLogger.createLogFile(PeerFolder.getTestConstant(), LogFolder.getTestConstant(), TestConstant.RequestLog.getTestConstant());
        AckMessage ackMessage = new AckMessage(Utilities.createUUID(), MessageFlag.Ack, Utilities.createTimestamp(), true);
        LogEntry logEntry = new LogEntry(ackMessage.getUuid(), ackMessage.getTimestamp(), ackMessage.getIsAck(), PeerName.getTestConstant(), "Bobby");
        boolean written = SessionLogger.writeEntry(logEntry.toString(), RequestLogPath.getTestConstant());
        System.out.println(written + ": " + logEntry);
        assertTrue(written);
    }

    @Test
    public void testIfAckMessageGetsSavedInContractFile() throws IOException {
        SessionLogger.createLogFile(PeerFolder.getTestConstant(), LogFolder.getTestConstant(), ContractLog.getTestConstant());
        AckMessage ackMessage = new AckMessage(Utilities.createUUID(), MessageFlag.Ack, Utilities.createTimestamp(), true);
        LogEntry logEntry = new LogEntry(ackMessage.getUuid(), ackMessage.getTimestamp(), ackMessage.getIsAck(), PeerName.getTestConstant(), "Bobby");
        boolean written = SessionLogger.writeEntry(logEntry.toString(), ContractLogPath.getTestConstant());
        System.out.println(written + ": " + logEntry);
        assertTrue(written);
    }

    @AfterAll
    public static void clear() throws IOException {
        String logPath = PeerFolder.getTestConstant() + "/" + LogFolder.getTestConstant();
        Files.delete(Paths.get(logPath + "/" + RequestLog.getTestConstant()));
        Files.delete(Paths.get(logPath + "/" + ContractLog.getTestConstant()));
     //   Files.delete(Paths.get(logPath));
    }
}
