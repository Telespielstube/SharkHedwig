package Message;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Message interface.
 *
 * @param <T>    Generic to work with all kinds of message objects.
 */
public interface Message<T> extends Serializable {

    /**
     * Creates a Version 4 (randomly generated) UUID which is an identifier that is
     * unique across both space and time
     *
     * @return    A 128-bit randomly created UUID.
     */
    UUID createUUID();
}

