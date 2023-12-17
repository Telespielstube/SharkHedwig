import Setup.Component;
import net.sharksystem.SharkException;
import net.sharksystem.SharkPeerFS;
import net.sharksystem.asap.ASAPPeer;
import net.sharksystem.pki.SharkPKIComponent;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

public class SharkComponentTestEnv {


    @Test
    public void setupComponentTest() throws SharkException {
        ASAPPeer asapPeer = null;
        SharkPKIComponent sharkPKIComponent = null;
        Collection<CharSequence> formats = new ArrayList<>();
        formats.add("sharkHedwig");
        String testFolder = "tester123" + "/" + "tester_id";
        SharkPeerFS testPeerFS = new SharkPeerFS("tester123", testFolder );
        Component component = new Component(sharkPKIComponent);
        component.setupComponent(testPeerFS);
        testPeerFS.start();
        //component.onStart(asapPeer);
    }
}
