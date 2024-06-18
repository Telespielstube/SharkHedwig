package Setup;

import Message.MessageCache;
import Misc.Utilities;
import Session.SessionManager;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * The MessageCacheCleaner works as a thread in the background. It allows to clean the stored messages after a specified time offset (in this case 2 min).
 * This regular checking of orphaned messages is intended to prevent an endless waiting loop in case a response message is lost while transferring.
 */
public class MessageCacheCleaner implements Runnable {

    private final SessionManager sessionManager;
    private ReentrantReadWriteLock lock;
    private long timeOffset = 120000; // one minute

    public MessageCacheCleaner(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.lock = new ReentrantReadWriteLock();
    }

    /**
     * The thread sleeps for the specified timeOffset and then checks whether the timestamp of the last entry in the message
     * cache has been exceeded. This prevents a dead lock in case some message will not be received by this device during
     * an ongoing session.
     */
    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(timeOffset);
                long now = System.currentTimeMillis();
                if (now - MessageCache.getLasElementKey() > timeOffset) {

                    // try finally block is necessary to prevent deadlocks.
                    try {
                        lock.writeLock().lock();
                        MessageCache.clearMessageList();
                        this.sessionManager.setSessionState(this.sessionManager.getNoSessionState());
                    } finally {
                        lock.writeLock().unlock();
                    }
                }
            } catch (InterruptedException e) {
                System.err.println(Utilities.formattedTimestamp() + "Caught an Exception while trying to clean up the cache: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }
}
