package SessionTest;

import Message.MessageHandler;
import Session.IdentificationSession.IdentificationSession;
import net.sharksystem.asap.ASAPPeer;
import net.sharksystem.pki.SharkPKIComponent;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class IdentificationSessionTest {

    private final MessageHandler messageHandler = new MessageHandler();
    private ASAPPeer peer;
    private SharkPKIComponent sharkPKIComponent;

    @Test
    public void testIfSecureRandomNumberIsReturnedAsString() {
        IdentificationSession identificationSession = new IdentificationSession("Peter", messageHandler, peer, sharkPKIComponent);
        System.out.println(identificationSession.generateRandomNumber());
        assertNotNull(identificationSession.generateRandomNumber());
    }
}
