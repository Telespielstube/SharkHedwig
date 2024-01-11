package Session;

/**
 * This is an enum class to handle the multiple states of the protocol (State machine pattern). This is a clean methode to avoid
 * global variables, which are difficult to handle and which we want to avoid with a primary focus on a secure protocol.
 * Every state has three methods to set the next state, get the current state and check if a state is completed.
 * The different states are:
 * - NoState: This is the initial state when no message exchange is going on.
 *
 */
public enum SessionState {

    NoSession {
        @Override
        public SessionState resetState() {
            return NoSession;
        }

        @Override
        public SessionState nextState() {
            return Identification;
        }
    },

    Identification {
        @Override
        public SessionState resetState() {
            return NoSession;
        }

        @Override
        public SessionState nextState() {
            return Request;
        }
    },

    Request {
        @Override
        public SessionState resetState() {
            return NoSession;
        }
        @Override
        public SessionState nextState() {
            return Contract;
        }
    },

    Contract() {
        @Override
        public SessionState resetState() {
            return NoSession;
        }

        @Override
        public SessionState nextState() {
            return NoSession;
        }
    };

    /**
     * Returns the current session state of the protocol.
     *
     * @return    Session state enum object.
     */
    public abstract SessionState resetState();


    /**
     * Proceeds to the next state.
     *
     * @return    Session State enum object.
     */
    public abstract SessionState nextState();

}
