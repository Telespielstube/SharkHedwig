package Setup;

import Misc.AppConstant;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.asap.pki.ASAPCertificate;
import net.sharksystem.pki.SharkPKIComponent;

public class PKIManager {
    SharkPKIComponent sharkPKIComponent;

    public PKIManager(SharkPKIComponent sharkPKIComponent) {
        this.sharkPKIComponent = sharkPKIComponent;
    }

    public CharSequence getCertificateAuthority() {
        return AppConstant.CA_ID.toString();
    }

    public ASAPCertificate getCertificateOf(CharSequence subject) throws ASAPSecurityException {
        return sharkPKIComponent.getCertificateByIssuerAndSubject(AppConstant.CA_ID.toString(), subject);
    }
}
