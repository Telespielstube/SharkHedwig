package MiscTest;

import Message.Contract.AckMessage;
import Message.MessageFlag;
import Misc.LogEntry;
import Misc.SessionLogger;
import Misc.Utilities;
import SetupTest.TestConstant;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static SetupTest.TestConstant.*;


public class SessionLoggerTest {

    @BeforeAll
    public void testIfDirectoryAndFileAreCreated() {
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
        assertTrue(written);
    }

    @AfterAll
    public void clear() {
        Path requestfilePath = Paths.get(String.format(PeerFolder.getTestConstant() + "/" + LogFolder.getTestConstant() + "/" + RequestLog.getTestConstant()));
       Path contractfilePath = Paths.get(String.format(PeerFolder.getTestConstant() + "/" + LogFolder.getTestConstant() + "/" + ContractLog.getTestConstant()));
        try {
            Files.delete(requestfilePath);
            //Files.delete(contractfilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
