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
        return AppConstant.CaId.toString();
    }

    public ASAPCertificate getCertificateOf(CharSequence subject) throws ASAPSecurityException {
        return sharkPKIComponent.getCertificateByIssuerAndSubject(AppConstant.CaId.toString(), subject);
    }
}
