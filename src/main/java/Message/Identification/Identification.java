package Message.Identification;

import Message.Message;
import Misc.Utilities;

import java.io.Serializable;
import java.util.UUID;

/**
 * Abstract class of the identitification messages object. This kind of message is used to exchange validate the
 * identities of two devices. This is achieved through a challenge response.
 */
abstract class Identification implements Message {

    @Override
    public UUID createUUID() {
        return UUID.randomUUID();
    }
}
