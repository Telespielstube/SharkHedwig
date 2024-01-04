package SetupTest;

import Session.Sessions.Identification;
import Setup.Channel;
import SetupTest.TestConstant;
import net.sharksystem.SharkComponent;
import net.sharksystem.SharkException;
import net.sharksystem.SharkPeer;
import net.sharksystem.SharkTestPeerFS;
import net.sharksystem.asap.crypto.ASAPKeyStore;
import net.sharksystem.pki.HelperPKITests;
import net.sharksystem.pki.SharkPKIComponent;
import net.sharksystem.pki.SharkPKIComponentFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class SharkComponentTest {

    ASAPKeyStore asapKeyStore;
    String francisID;
    PublicKey publicKeyFrancis;
    Identification identification;


    @Test
    public void setup() throws SharkException, IOException, NoSuchPaddingException, NoSuchAlgorithmException {

        SharkTestPeerFS aliceSharkPeer = new SharkTestPeerFS(TestConstant.PeerName.getTestConstant(), TestConstant.PeerFolder.getTestConstant());
        SharkPKIComponent sharkPKIComponent = setupComponent(aliceSharkPeer);

        aliceSharkPeer.start();

        String idStart = HelperPKITests.fillWithExampleData(sharkPKIComponent);

        asapKeyStore = sharkPKIComponent.getASAPKeyStore();
        francisID = HelperPKITests.getPeerID(idStart, HelperPKITests.FRANCIS_NAME);
        publicKeyFrancis = asapKeyStore.getPublicKey(francisID);
        identification = new Identification(sharkPKIComponent);
    }

    private SharkPKIComponent setupComponent(SharkPeer sharkPeer) throws SharkException, NoSuchPaddingException, NoSuchAlgorithmException {
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
        return (SharkPKIComponent) component;
    }


    @Test
    public void testIfChannelAdvertisementEqualsReceivedURI() {
        String uri = "sn2://Advertisement";
        assertEquals(uri, Channel.Advertisement.getChannelType());
    }

    @Test
    public void clearUp() {
        File certificates = Paths.get(String.format("./src/test/resources/TestStorage/ASAPSharkCertificates")).toFile();
        File credentials = Paths.get(String.format("./src/test/resources/TestStorage/SharkCredentials")).toFile();
        certificates.deleteOnExit();
        credentials.deleteOnExit();
    }
}
