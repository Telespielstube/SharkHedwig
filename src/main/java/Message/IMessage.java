package Message;

import java.io.Serializable;
import java.util.UUID;

/**
 * Message interface.
 */
public interface IMessage<T> extends Serializable {

    /**
     * Creates a Version 4 (randomly generated) UUID which is an identifier that is
     * unique across both space and time
     *
     * @return    A 128-bit randomly created UUID.
     */
    UUID createUUID();
    
    UUID getUuid();
    int getFlag();

    long getTimestamp();


}

