package MiscTest;

import Misc.ErrorLogger;
import org.junit.Test;
import org.junit.jupiter.api.io.TempDir;

public class LogFileTest {


    @Test
    public void testIfErrorFolderAndFileGetCreated() {
        String tempFolder = "TempFolder";
        String tempLog = "TempLog";
        String tempError = "tempError.txt";
        ErrorLogger.redirectErrorStream(tempFolder,tempLog, tempError);
        System.err.println("Test");

    }

}
