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

    NO_SESSION {
        @Override
        public SessionState resetState() {
            return NO_SESSION;
        }

        public SessionState nextState() {
            return AUTHENTIFICATION;
        }
    },

    AUTHENTIFICATION {
        @Override
        public SessionState resetState() { return NO_SESSION; }
        @Override
        public SessionState nextState() { return REQUEST; }
    },

    REQUEST {
        @Override
        public SessionState resetState() {
            return NO_SESSION;
        }
        @Override
        public SessionState nextState() {
            return CONTRACT;
        }
    },

    CONTRACT() {
        @Override
        public SessionState resetState() {
            return NO_SESSION;
        }

        @Override
        public SessionState nextState() {
            return NO_SESSION;
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
