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
            SessionLogger.createLogDirectory(TestConstant.PeerFolder.getTestConstant(), LogFolder.getTestConstant());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testIfAckMessageGetsSavedInRequestFile(){
        try {
            SessionLogger.createLogFile(TestConstant.PeerFolder.getTestConstant(), LogFolder.getTestConstant(), TestConstant.RequestLog.getTestConstant());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        AckMessage ackMessage = new AckMessage(Utilities.createUUID(), MessageFlag.Ack, Utilities.createTimestamp(), true);
        LogEntry logEntry = new LogEntry(ackMessage.getUuid(), ackMessage.getTimestamp(), ackMessage.getIsAck(), TestConstant.PeerName.getTestConstant(), "Bobby");
        boolean written = SessionLogger.writeEntry(logEntry.toString(), RequestLogPath.getTestConstant());
        System.out.println(written + ": " + logEntry);
        assertTrue(written);
    }

    @Test
    public void testIfAckMessageGetsSavedInContractFile(){
        try {
            SessionLogger.createLogFile(TestConstant.PeerFolder.getTestConstant(), LogFolder.getTestConstant(), TestConstant.ContractLog.getTestConstant());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        AckMessage ackMessage = new AckMessage(Utilities.createUUID(), MessageFlag.Ack, Utilities.createTimestamp(), true);
        LogEntry logEntry = new LogEntry(ackMessage.getUuid(), ackMessage.getTimestamp(), ackMessage.getIsAck(), TestConstant.PeerName.getTestConstant(), "Bobby");
        boolean written = SessionLogger.writeEntry(logEntry.toString(), ContractLogPath.getTestConstant());
        System.out.println(written + ": " + logEntry);
        assertTrue(written);
    }

    @AfterAll
    public static void clear() throws IOException {
        Path requestFilePath = Paths.get(String.format(PeerFolder.getTestConstant() + "/" + LogFolder.getTestConstant() + "/" + RequestLog.getTestConstant()));
        Path contractFilePath = Paths.get(String.format(PeerFolder.getTestConstant() + "/" + LogFolder.getTestConstant() + "/" + ContractLog.getTestConstant()));
        File logFile = Paths.get(String.format(TestConstant.PeerFolder.getTestConstant() +"/"+ LogFolder.getTestConstant())).toFile();
        logFile.deleteOnExit();
        Files.delete(requestFilePath);
        Files.delete(contractFilePath);
    }
}
