package MiscTest;

import Misc.ErrorLogger;
import SetupTest.TestConstant;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;

import static SetupTest.TestConstant.*;
import static SetupTest.TestConstant.ContractLog;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ErrorLoggerTest {
    private static String filePath = TestConstant.PeerFolder.getTestConstant() + "/" + TestConstant.LogFolder.getTestConstant() + "/" + TestConstant.ErrorLog.getTestConstant();
    private static String path = TestConstant.PeerFolder.getTestConstant() + "/" + TestConstant.LogFolder.getTestConstant();

    @Test
    public void testIfErrorFolderAndFileGetCreated() {
        Path filePath = Paths.get(path);
        ErrorLogger.redirectErrorStream(TestConstant.PeerFolder.getTestConstant(), TestConstant.LogFolder.getTestConstant(), TestConstant.ErrorLog.getTestConstant());
        System.err.println("Test to check if the error stream gets redirected");
        assertNotNull(Files.exists(filePath));
    }

    @AfterAll
    public static void clear() throws IOException {
        Files.delete(Paths.get(filePath));
    }
}
