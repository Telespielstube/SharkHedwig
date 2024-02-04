import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.asap.crypto.ASAPKeyStore;
import net.sharksystem.asap.crypto.InMemoASAPKeyStore;
import net.sharksystem.asap.persons.ASAPCertificateStore;
import net.sharksystem.asap.persons.ASAPCertificateStoreImpl;
import net.sharksystem.asap.pki.ASAPCertificateStorage;
import net.sharksystem.asap.pki.CredentialMessageInMemo;
import net.sharksystem.asap.pki.InMemoAbstractCertificateStore;
import net.sharksystem.pki.SharkPKIComponent;

import java.io.IOException;
import java.util.Random;

/**
 * This is an imported test class from the SharkPKI project which is part of the ASAP/Shark Framework
 * link: https://github.com/SharedKnowledge. This helper class provides example data to use test the public key infrastructure.
 */
public class HelperPKITests {
    public static final CharSequence FRANCIS_NAME = "Francis";
    public static String getPeerID(String idStart, CharSequence peerName) {
        return idStart + peerName;
    }

    public static String fillWithExampleData(SharkPKIComponent asapPKI)
            throws ASAPSecurityException, IOException {

        ASAPCertificateStorage certificateStorage;
        long now = System.currentTimeMillis();

        Random random = new Random(System.currentTimeMillis());
        String randomString = random.toString();

        // very very unlikely, but better safe than sorry: example data must same id
        String idStart = randomString.substring(0, 3) + "_";

        // Owner signs Francis ia(F): 10
        String francisID = getPeerID(idStart, FRANCIS_NAME);

        // asap storage - certificate container
        certificateStorage = new InMemoAbstractCertificateStore(francisID, FRANCIS_NAME);

        // a source of keys for francis
        ASAPKeyStore francisCryptoStorage = new InMemoASAPKeyStore(FRANCIS_NAME);

        // put certificates and keystore together and set up Francis' PKI
        ASAPCertificateStore francisStorage = new ASAPCertificateStoreImpl(certificateStorage, francisCryptoStorage);

        // produce Francis' public key which isn't used but signed by target PKI
        asapPKI.acceptAndSignCredential(
                new CredentialMessageInMemo(francisID, FRANCIS_NAME, now, francisStorage.getPublicKey()));

        return idStart;
    }
}
