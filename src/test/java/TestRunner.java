import SetupTest.ComponentTest;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

/**
 * This class runs all test cases automatically. No need to
 */
@Suite
@SelectPackages({"DeliveryContractTest", "HedwigUITest","LocationTest","MessageTest"})
public class TestRunner {}


