package Message;

import Session.Sessions.AbstractSession;

import java.io.Serializable;
import java.util.UUID;

/**
 * Message interface.
 */
public interface IMessage extends Serializable {

    /**
     * Returns s a Version 4 (randomly generated) UUID which is an identifier that is
     * unique across both space and time
     *
     * @return    A 128-bit randomly created UUID.
     */
    UUID getUuid();

    /**
     * Returns the message flag.
     *
     * @return Message flag.
     */
    MessageFlag getMessageFlag();

    /**
     * Sets the appropriate message flag in the concrete class implementation.
     *
     * @param messageFlag    Integer value of the message type.
     */
    void setMessageFlag(MessageFlag messageFlag);

    /**
     * Returns a timestamp in UNIX format
     *
     * @return    current time in millis.
     */
    long getTimestamp();

    /**
     * Sets the timestamp for the message.
     *
     * @param currentTime    Current time in milliseconds.
     */
    void setTimestamp(long currentTime);
}
