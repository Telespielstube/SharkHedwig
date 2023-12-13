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
        public SessionState currentState() {
            return NoSession;
        }

        @Override
        public SessionState nextState() {
            return Identification;
        }
        @Override
        public boolean stateCompleted() {
            return true;
        }
    },

    Identification {
        @Override
        public SessionState currentState() {
            return Identification;
        }

        @Override
        public SessionState nextState() {
            return Request;
        }
        @Override
        public boolean stateCompleted() {
            return true;
        }
    },

    Request {
        @Override
        public SessionState currentState() {
            return Request;
        }
        @Override
        public SessionState nextState() {
            return Handover;
        }
        @Override
        public boolean stateCompleted() {
            return true;
        }
    },

    Handover() {
        @Override
        public SessionState currentState() {
            return Handover;
        }

        @Override
        public SessionState nextState() {
            return NoSession;
        }

        @Override
        public boolean stateCompleted() {
            return true;
        }


    };

    /**
     * Returns the current session state of the protocol.
     *
     * @return    Session state enum object.
     */
    public abstract SessionState currentState();


    /**
     * Proceeds to the next state.
     *
     * @return    Session State enum object.
     */
    public abstract SessionState nextState();

    /**
     * Returns a boolean value if session is completed.
     *
     * @return    Boolean value true if session is completed.
     */
    public abstract boolean stateCompleted();
}
