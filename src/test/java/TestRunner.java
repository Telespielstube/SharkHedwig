
import org.junit.jupiter.api.AfterAll;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

/**
 * This class runs all test cases automatically.
 */

@Suite
@SelectPackages({"DeliveryContractTest","LocationTest","MessageTest", "MiscTest",
        "SessionTest", "SetupTest"})
public class TestRunner {}





