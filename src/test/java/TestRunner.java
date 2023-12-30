import DeliveryContractTest.*;
import HedwigUITest.UserInterfaceTest;
import LocationTest.*;
import MessageTest.MessageFlagTest;
import MessageTest.MessageHandlerTest;
import MessageTest.MessageTest;
import Misc.Utilities;
import MiscTest.ErrorLoggerTest;
import MiscTest.SessionLoggerTest;
import SessionTest.*;
import SetupTest.ChannelTest;
import SetupTest.ComponentTest;
import SetupTest.ConstantTest;
import SetupTest.DeviceStateTest;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

import java.util.Vector;

/**
 * This class runs all test cases automatically. No need to
 */
@Suite
@SelectPackages({"DeliveryContractTest", "HedwigUITest","LocationTest","MessageTest"})
public class TestRunner {}


