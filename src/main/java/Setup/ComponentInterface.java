package Setup;

import Misc.Constants;
import net.sharksystem.ASAPFormats;
import net.sharksystem.SharkComponent;

@ASAPFormats(formats = {Constants.APP_FORMAT})
public interface ComponentInterface extends SharkComponent {

    /** start the contract exchange session. 1. Identification 2. Delivery contract exchange. Every step runs automatically */
//    void startSession(CharSequence transferee, CharSequence packageID) throws IOException, ASAPException;
//    /** create Delivery contract for a package. This happens before any contract exchange*/
//
//    void initiateAndSaveDeliveryContract(CharSequence e2eReceiver, CharSequence packageID, Location e2eReceiverLocation) throws ASAPException;
//
//    /** return all Delivery contract in storage*/
//    ArrayList<DeliveryContract> getAllDeliveryContract() throws IOException, ClassNotFoundException;
}
