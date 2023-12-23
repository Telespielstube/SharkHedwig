package MiscTest;

import Misc.ErrorLogger;
import SetupTest.TestConstant;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertNotNull;

public class ErrorLoggerTest {


    @Test
    public void testIfErrorFolderAndFileGetCreated() {
        Path filePath = Paths.get(String.format(TestConstant.PeerFolder.getTestConstant() + "/" + TestConstant.LogFolder.getTestConstant() + "/" + TestConstant.ErrorLog.getTestConstant()));
        ErrorLogger.redirectErrorStream(TestConstant.PeerFolder.getTestConstant(), TestConstant.LogFolder.getTestConstant(), TestConstant.ErrorLog.getTestConstant());
        System.err.println("Test to check if the error stream gets redirected");
        assertNotNull(Files.exists(filePath));
    }

    @After
    public void clear() {
        Path filePath = Paths.get(String.format(TestConstant.PeerFolder.getTestConstant() + "/" + TestConstant.LogFolder.getTestConstant() + "/" + TestConstant.ErrorLog.getTestConstant()));
        try {
            Files.delete(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
