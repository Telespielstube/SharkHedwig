package Setup;

import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.asap.pki.ASAPCertificate;
import net.sharksystem.pki.SharkPKIComponent;

public class PKIManager {
    SharkPKIComponent sharkPKIComponent;

    public PKIManager(SharkPKIComponent sharkPKIComponent) {
        this.sharkPKIComponent = sharkPKIComponent;
    }

    public CharSequence getCertificateAuthority() {
        return Constant.CaId.getAppConstant();
    }

    public ASAPCertificate getCertificateOf(CharSequence subject) throws ASAPSecurityException {
        return sharkPKIComponent.getCertificateByIssuerAndSubject(Constant.CaId.getAppConstant(), subject);
    }
}
