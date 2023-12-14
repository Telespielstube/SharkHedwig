package Setup;

import Message.Identification.*;
import Message.Request.Request;
import net.sharksystem.SharkException;
import net.sharksystem.SharkPeerFS;
import net.sharksystem.asap.ASAPPeer;
import net.sharksystem.pki.SharkPKIComponent;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

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
        assertEquals(uri, Channel.Advertisement.getChannelType());
    }

//    private final Challenge challenge = new Challenge(UUID.randomUUID(), System.currentTimeMillis());
//    private final Response response = new Response(UUID.randomUUID(), System.currentTimeMillis());
//   // private final Request request = new Request();
//    @Test
//    public void returnTrueIfChallengeMessageIsOfTypeIdentification() {
//        assertTrue(challenge instanceof AbstractIdentification);
//    }
//
//    @Test
//    public void returnTrueIfResponseMessageIsOfTypeIdentification() {
//        assertTrue(response instanceof AbstractIdentification);
//    }
}
