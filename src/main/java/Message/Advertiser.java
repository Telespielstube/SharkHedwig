package Message;

import Message.NoSession.Advertisement;
import Misc.Utilities;
import ProtocolRole.ProtocolRole;
import Setup.AppConstant;
import Setup.Channel;
import Setup.SharkHedwigComponent;

import java.util.Optional;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * This thread manages the Advertisement message management. Every 10 min the thread checks if an andvertisemnt message
 * is stored in the message cache and depending in the role a message is created and sent.
 */
public class Advertiser implements Runnable {

    private final ProtocolRole protocolRole;
    private final SharkHedwigComponent sharkHedwigComponent;
    private ReentrantReadWriteLock lock;

    public Advertiser(SharkHedwigComponent sharkHedwigComponent, ProtocolRole protocolRole) {
        this.protocolRole = protocolRole;
        this.sharkHedwigComponent = sharkHedwigComponent;
        this.lock = new ReentrantReadWriteLock();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(10000);
                try {
                    lock.readLock().lock();
                    if (MessageCache.getLastElementFromList() instanceof Advertisement) {
                        break;
                    }
                } finally {
                    lock.readLock().unlock();
                }
                if (this.protocolRole.equals(this.protocolRole.getTransfereeState()) && MessageCache.getMessageCacheSize() == 0) {
                    Optional<Advertisement> optionalMessage = Optional.of(new Advertisement(Utilities.createUUID(),
                            MessageFlag.ADVERTISEMENT, Utilities.createTimestamp(), true));
                    try {
                        lock.writeLock().lock();
                        MessageCache.addMessage(optionalMessage.get());
                        sharkHedwigComponent.outgoingMessage(Optional.of(new MessageBuilder(optionalMessage,
                                Channel.NO_SESSION.getChannel(), AppConstant.PEER_NAME.toString())));
                    } finally {
                        lock.writeLock().unlock();
                    }
                }
            } catch (InterruptedException e) {
                System.err.println(Utilities.formattedTimestamp() + "Caught an Exception while running the Advertisement " +
                        "message thread: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }
}
