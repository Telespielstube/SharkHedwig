import SetupTest.SharkComponentTest;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

/**
 * This class runs all test cases automatically. No need to
 */
@Suite
@SelectPackages({"DeliveryContractTest","LocationTest","MessageTest", "MiscTest", "SessionTest", "SetupTest"})
public class TestRunner {}


