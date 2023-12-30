package SetupTest;

import Message.Advertisement;
import Message.MessageFlag;
import Message.MessageHandler;
import Misc.Utilities;
import Session.SessionManager;
import Session.SessionState;
import Session.Sessions.Identification;
import Setup.Channel;
import Setup.Component;
import Setup.DeviceState;
import net.sharksystem.SharkComponent;
import net.sharksystem.SharkException;
import net.sharksystem.SharkPeer;
import net.sharksystem.SharkTestPeerFS;
import net.sharksystem.asap.crypto.ASAPKeyStore;
import net.sharksystem.pki.HelperPKITests;
import net.sharksystem.pki.SharkPKIComponent;
import net.sharksystem.pki.SharkPKIComponentFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComponentTest {

    private static PublicKey publicKeyFrancis;
    private static Component component;
    private static String francisID;
    private static Identification identification;
    private static SharkPKIComponent sharkPKIComponent;
  //  private static MessageHandler messageHandler;

    @BeforeAll
    public static void setup() throws SharkException, IOException, NoSuchPaddingException, NoSuchAlgorithmException {

        SharkTestPeerFS aliceSharkPeer = new SharkTestPeerFS("Alice", "tester123/Alice");
        sharkPKIComponent = setupComponent(aliceSharkPeer);

        aliceSharkPeer.start();

        String idStart = HelperPKITests.fillWithExampleData(sharkPKIComponent);

        ASAPKeyStore asapKeyStore = sharkPKIComponent.getASAPKeyStore();
        francisID = HelperPKITests.getPeerID(idStart, HelperPKITests.FRANCIS_NAME);
        publicKeyFrancis = asapKeyStore.getPublicKey(francisID);
        identification = new Identification(sharkPKIComponent);

    }

    private static SharkPKIComponent setupComponent(SharkPeer sharkPeer) throws SharkException, NoSuchPaddingException, NoSuchAlgorithmException {
        SharkPKIComponentFactory certificateComponentFactory = new SharkPKIComponentFactory();

        // register this component with shark peer - note: we use interface SharkPeer
        try {
            sharkPeer.addComponent(certificateComponentFactory, SharkPKIComponent.class);
        } catch (SharkException e) {
            throw new RuntimeException(e);
        }

        // get component instance
        SharkComponent pkiComponent = sharkPeer.getComponent(SharkPKIComponent.class);

        // project "clean code" :) we only use interfaces - unfortunately casting is unavoidable

        return (SharkPKIComponent) pkiComponent;
    }

    @Test
    public void testIfChannelAdvertisementEqualsReceivedURI() {
        String uri = "sn2://Advertisement";
        assertEquals(uri, Channel.Advertisement.getChannelType());
    }

    @Test
    public void testIfReceivedMessageGetsHAndledCorrectly() throws NoSuchPaddingException, NoSuchAlgorithmException {
        ASAPMessageList messageList = new ASAPMessageList();
        MessageHandler messageHandler = new MessageHandler();
        component = new Component(sharkPKIComponent);
        //Simulated ASAPMessages list
        messageList.addMessage(messageHandler.objectToByteArray(new Advertisement(Utilities.createUUID(), MessageFlag.Advertisement, Utilities.createTimestamp(), true)));

        component.processMessages(messageList, francisID);
    }
}
