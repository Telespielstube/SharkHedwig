package Message;

import java.io.Serializable;
import java.util.UUID;

/**
 * Message interface.
 */
public interface IMessage extends Serializable {

    /**
     * Creates a Version 4 (randomly generated) UUID which is an identifier that is
     * unique across both space and time
     *
     * @return    A 128-bit randomly created UUID.
     */
    UUID createUUID();

    /**
     * Returns s a Version 4 (randomly generated) UUID which is an identifier that is
     * unique across both space and time
     *
     * @return    A 128-bit randomly created UUID.
     */
    UUID getUuid();

    /** Returns the message flag.
     *
     * @return    Message flag.
     */
    int getFlag();

    /**
     * Returns a timestamp in UNIX format
     *
     * @return    current time in millis.
     */
    long getTimestamp();
}
