package MessageTest;

import Message.Identification.Challenge;
import Message.MessageFlag;
import Message.MessageHandler;
import SetupTest.TestConstant;
import net.sharksystem.SharkComponent;
import net.sharksystem.SharkException;
import net.sharksystem.SharkPeer;
import net.sharksystem.SharkTestPeerFS;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.asap.crypto.ASAPKeyStore;
import net.sharksystem.pki.HelperPKITests;
import net.sharksystem.pki.SharkPKIComponent;
import net.sharksystem.pki.SharkPKIComponentFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.security.PublicKey;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class MessageHandlerTest {

    private final String ALICE_ID = "ALICE_44";

    private final Challenge challenge = new Challenge(UUID.randomUUID(), MessageFlag.Challenge, System.currentTimeMillis(), "342532452345".getBytes());
    private MessageHandler messageHandler = new MessageHandler();
    private SharkPKIComponent sharkPKIComponent;
    private ASAPKeyStore asapKeyStore;
    private SharkPeer sharkTestPeer;
    private String francisID;
    PublicKey publicKeyFrancis;

    @Before
    public void setup() throws SharkException, IOException {

        sharkTestPeer = new SharkTestPeerFS(TestConstant.PeerName.getTestConstant(), TestConstant.PeerFolder.getTestConstant() + "/" + TestConstant.PeerName.getTestConstant());
        sharkPKIComponent = setupComponent(sharkTestPeer);

        sharkTestPeer.start();

        String idStart = HelperPKITests.fillWithExampleData(sharkPKIComponent);

        asapKeyStore = sharkPKIComponent.getASAPKeyStore();
        francisID = HelperPKITests.getPeerID(idStart, HelperPKITests.FRANCIS_NAME);
        publicKeyFrancis = asapKeyStore.getPublicKey(francisID);
    }

    private SharkPKIComponent setupComponent(SharkPeer sharkPeer) throws SharkException {
        SharkPKIComponentFactory certificateComponentFactory = new SharkPKIComponentFactory();

        // register this component with shark peer - note: we use interface SharkPeer
        try {
            sharkPeer.addComponent(certificateComponentFactory, SharkPKIComponent.class);
        } catch (SharkException e) {
            throw new RuntimeException(e);
        }

        // get component instance
        SharkComponent component = sharkPeer.getComponent(SharkPKIComponent.class);

        // project "clean code" :) we only use interfaces - unfortunately casting is unavoidable
        SharkPKIComponent sharkPKIComponent = (SharkPKIComponent) component;

        return sharkPKIComponent;
    }

    @Test
    public void testIfMessageGetsBuildForSending() {
        byte[] outgoingMessage = messageHandler.buildOutgoingMessage(challenge, MessageFlag.Challenge.toString(), francisID, sharkPKIComponent);
        assertNotEquals(outgoingMessage.toString(), challenge.toString());
    }


    // There is something wrong decrypting a incoming message!!
    @Test
    public void testIfByteMessageGetsParsedToMessageObject() throws ASAPSecurityException {
        byte[] outgoingMessage = messageHandler.buildOutgoingMessage(challenge, MessageFlag.Challenge.toString(), francisID, sharkPKIComponent);
        System.out.println(outgoingMessage.toString());
        Object object = messageHandler.parseMessage(outgoingMessage, francisID, sharkPKIComponent);

        assertEquals(object.getClass(), challenge.getClass());
    }
    @Test
    public void testIfObjectGetsConvertedToByteArrayAndBackToObject() {
        byte[] byteMessage = messageHandler.objectToByteArray(challenge);
        Object object = messageHandler.byteArrayToObject(byteMessage);
        assertEquals(object.getClass(), challenge.getClass());
    }
}
