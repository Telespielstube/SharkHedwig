package Message;

import java.util.UUID;

public class Solicitation extends Message {
    private boolean solicitate = false;

    public Solicitation(UUID uuid, MessageFlag messageFlag, long timestamp, boolean solicitate) {
        super(uuid, messageFlag, timestamp);
        this.solicitate = solicitate;
    }

    public boolean getSolicitate() {
        return this.solicitate;
    }
}

