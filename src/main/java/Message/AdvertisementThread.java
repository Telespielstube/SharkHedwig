package Message;

import Message.NoSession.Advertisement;
import Misc.Utilities;
import ProtocolRole.ProtocolRole;
import Setup.AppConstant;
import Setup.Channel;
import Setup.SharkHedwigComponent;

import java.util.Optional;

/**
 * This thread manages the Advertisement message management. Every 10 min the thread checks if an andvertisemnt message
 * is stored in the message cache and depending in the role a message is created and sent.
 */
public class AdvertisementThread implements Runnable {

    private final ProtocolRole protocolRole;
    private final SharkHedwigComponent sharkHedwigComponent;

    public AdvertisementThread(SharkHedwigComponent sharkHedwigComponent, ProtocolRole protocolRole) {
        this.protocolRole = protocolRole;
        this.sharkHedwigComponent = sharkHedwigComponent;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(100000);
                if (MessageCache.getLastValueFromList() instanceof Advertisement) {
                    break;
                }
                if (this.protocolRole.equals(this.protocolRole.getTransfereeState())) {
                    Optional<MessageBuilder> optionalMessage = Optional.of(new MessageBuilder(Optional.of(
                            new Advertisement(Utilities.createUUID(), MessageFlag.ADVERTISEMENT, Utilities.createTimestamp(),
                                    true)), Channel.NO_SESSION.getChannel(), AppConstant.PEER_NAME.toString()));
                    if (optionalMessage.isPresent()) {
                        sharkHedwigComponent.outgoingMessage(optionalMessage);
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
