package Session;

import static Misc.Constants.*;

/**
 * This is an enum class to handle the multiple states of the protocol (State machine pattern). This is probably a nice and clean methode to avoid
 * global variables, which are difficult to handle and which we want to avoid with a primary focus on a secure protocol.
 */
public enum SessionState {

    NoState {
        @Override
        public int getState() {
            return NO_SESSION_FLAG;
        }

        @Override
        public int nextState() {
            return IDENTIFICATION_FLAG;
        }
    },

    Identification {
        @Override
        public int getState() {
            return IDENTIFICATION_FLAG;
        }

        @Override
        public int nextState() {
            return REQUEST_FLAG;
        }
    },

    Request {
        @Override
        public int getState() {
            return REQUEST_FLAG;
        }
        @Override
        public int nextState() {
            return HANDOVER_FLAG;
        }
    },

    Handover() {
        @Override
        public int getState() {
            return HANDOVER_FLAG;
        }

        @Override
        public int nextState() {
            return NO_SESSION_FLAG;
        }
    };

    public abstract int getState();
    public abstract int nextState();
}
