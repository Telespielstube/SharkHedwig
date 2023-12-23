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
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.util.Vector;


/**
 * This class runs all test cases automatically. No need to
 */
public class TestRunner {
    public static void main(String[] args) {
        {
            Vector<Class> testCases = new Vector<>();

            // All test classes
            // DeliveryContractTest
            testCases.add(TransitRecordTest.class);
            testCases.add(TransitEntryTest.class);
            testCases.add(DeliveryContractTest.class);
            // UserInterfaceTest
            testCases.add(UserInterfaceTest.class);
            //LocationTest
            testCases.add(LocationTest.class);
            testCases.add(ParallelEnumTest.class);
            // MessageTest
            testCases.add(MessageFlagTest.class);
            testCases.add(MessageHandlerTest.class);
            testCases.add(MessageTest.class);
            // MiscTest
            testCases.add(ErrorLoggerTest.class);
            testCases.add(SessionLoggerTest.class);
            testCases.add(Utilities.class);
            // SessionTest
            testCases.add(IdentificationStateTest.class);
            testCases.add(IdentificationTest.class);
            testCases.add(SessionManagerTest.class);
            // SetupTest
            testCases.add(ChannelTest.class);
            testCases.add(ComponentTest.class);
            testCases.add(ConstantTest.class);
            testCases.add(DeviceStateTest.class);

            for (Class testCase : testCases)
            {
                runTestCases(testCase);
            }
        }
    }

    private static void runTestCases(Class testCase) {
        Result result = JUnitCore.runClasses( testCase);
        for (Failure failure : result.getFailures()) {
            System.err.println("Test case failed: " + failure.toString());
        }
        System.out.println("Test cases run successfully!!");
    }
}
