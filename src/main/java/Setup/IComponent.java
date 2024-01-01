package Setup;

import net.sharksystem.ASAPFormats;
import net.sharksystem.SharkComponent;
import net.sharksystem.SharkException;
import net.sharksystem.SharkPeerFS;
import net.sharksystem.asap.ASAPHop;
import net.sharksystem.asap.ASAPMessages;
import net.sharksystem.asap.ASAPPeer;

import java.io.IOException;
import java.util.List;

@ASAPFormats(formats = {"hedwig"})
public interface IComponent extends SharkComponent {

    /**
     * Setup process for the SkarkPKIComponent.
     *
     * @param sharkPeerFS SharkPeerFS Object that the PKIComponent can be created.
     * @throws SharkException Is thrown if something goes wrong during the setup process.
     */
    void setupComponent(SharkPeerFS sharkPeerFS);

    /**
     * This methode is called from within the methode SharkPeerFS to get the component
     * and its setup process started.
     * <p>
     * ASAPPeer  Describes the actual Peer
     */
    @Override
    void onStart(ASAPPeer asapPeer) throws SharkException;

    /**
     * Setting up all component channels. Multiple channels allow us to control incoming and outgoing messages much better.
     */
    void setupChannel();

    /**
     * Setting up all things logging. The folder and the files to differentiate between request session and contract session.
     */
    void setupLogger();

    /**
     * The methode parses the byte[] message to a Message object and passes it to the SEssionManager class.
     *
     * @param messages    received message as byte[] data type..
     * @param senderE2E   The device which sent the received message.
     * @return
     * @throws IOException
     */
    void processMessages(ASAPMessages messages, String senderE2E);
}
