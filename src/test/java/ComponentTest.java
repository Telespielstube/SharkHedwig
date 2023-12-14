import Setup.Component;
import net.sharksystem.SharkException;
import net.sharksystem.SharkPeerFS;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.ASAPPeerFS;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class ComponentTest {
//    String ROOT_DIRECTORY = "testResultsRootFolder/";
//    String ALICE_ID = "Alice_42";
//    String ALICE_NAME = "Alice";
//    String BOB_ID = "Bob_43";
//    String BOB_NAME = "Bob";
//
//    String URI = "shark://testUri";
//    byte[] MESSAGE_1 = "1st message".getBytes();
//    byte[] MESSAGE_2 = "2nd message".getBytes();

    @Test
    public void setupComponent() {
        Collection<CharSequence> formats = new ArrayList<>();
        formats.add("sharkHedwig");
        String testFolder = "tester123" + "/" + "tester_id";
        try {
            SharkPeerFS testPeerFS = new SharkPeerFS("terster123", testFolder );
            //ASAPPeerFS testPeerFS = new ASAPPeerFS("Marta", "martasFolder", formats);
            try {
                new Component().setupComponent(testPeerFS);
            } catch (SharkException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException | ASAPException e) {
            throw new RuntimeException(e);
        }
    }
}
