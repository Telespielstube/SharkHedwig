package SetupTest;

import Setup.Channel;
import Setup.Component;
import net.sharksystem.SharkException;
import net.sharksystem.SharkPeerFS;
import net.sharksystem.asap.ASAPPeer;
import net.sharksystem.pki.SharkPKIComponent;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ComponentTest {

    @Test
    public void setupComponent() throws SharkException {
        ASAPPeer asapPeer = null;
        SharkPKIComponent sharkPKIComponent = null;
        Collection<CharSequence> formats = new ArrayList<>();
        formats.add("sharkHedwig");
        String testFolder = "tester123" + "/" + "tester_id";
        SharkPeerFS testPeerFS = new SharkPeerFS("tester123", testFolder );
        Component component = new Component(sharkPKIComponent);
        component.setupComponent(testPeerFS);
        testPeerFS.start();
    }

    @Test
    public void testIfChannelAdvertisementEqualsReceivedURI() {
        String uri = "sn2://Advertisement";
        Assert.assertEquals(uri, Channel.Advertisement.getChannelType());
    }
}
