package Setup;

import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.asap.pki.ASAPCertificate;
import net.sharksystem.pki.SharkPKIComponent;

public class PKIManager {
    CharSequence caID;
    SharkPKIComponent sharkPKIComponent;

    public PKIManager(CharSequence caID, SharkPKIComponent sharkPKIComponent) {
        this.caID = caID;
        this.sharkPKIComponent = sharkPKIComponent;
    }

    public CharSequence getCertificateAuthority() {
        return this.caID;
    }

    public ASAPCertificate getCertificateOf(CharSequence subject) throws ASAPSecurityException {
        return sharkPKIComponent.getCertificateByIssuerAndSubject(caID, subject);
    }
}
