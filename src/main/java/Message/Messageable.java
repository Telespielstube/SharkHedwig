package Message;

import java.io.Serializable;
import java.util.UUID;

/**
 * Message interface.
 */
public interface Messageable extends Serializable {

    /**
     * Returns s a Version 4 (randomly generated) UUID which is an identifier that is
     * unique across both space and time
     *
     * @return    A 128-bit randomly created UUID.
     */
    UUID getUUID();

    /**
     * Returns the message flag.
     *
     * @return Message flag.
     */
    MessageFlag getMessageFlag();

    /**
     * Returns a timestamp in UNIX format
     *
     * @return    current time in millis.
     */
    long getTimestamp();
}
