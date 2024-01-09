import SetupTest.SharkComponentTest;
import SetupTest.TestConstant;
import org.junit.jupiter.api.AfterAll;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class runs all test cases automatically. No need to
 */
@Suite
@SelectPackages({"DeliveryContractTest","LocationTest","MessageTest", "MiscTest", "SessionTest", "SetupTest"})
public class TestRunner {

    public static void deleteTestStorage() throws IOException {
        Path path = Paths.get(TestConstant.PeerFolder.getTestConstant());
        Files.delete(Paths.get(path + "/" + "SharkCredentials/asapCurrentAttributes"));
        Files.delete(Paths.get(path + "/" + "SharkCredentials"));
    }
}




