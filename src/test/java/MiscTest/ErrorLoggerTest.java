package MiscTest;

import Misc.ErrorLogger;
import SetupTest.TestConstant;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ErrorLoggerTest {
    private static final String filePath = TestConstant.PeerFolder.getTestConstant() + "/" + TestConstant.LogFolder.getTestConstant() + "/" + TestConstant.ErrorLog.getTestConstant();

    @Test
    public void testIfErrorFolderAndFileGetCreated() {
        ErrorLogger.redirectErrorStream(TestConstant.PeerFolder.getTestConstant(), TestConstant.LogFolder.getTestConstant(), TestConstant.ErrorLog.getTestConstant());
        System.err.println("Test to check if the error stream gets redirected");
        Files.exists(Paths.get(filePath));
    }

    @AfterAll
    public static void clear() throws IOException {
        Files.delete(Paths.get(filePath));
    }
}
