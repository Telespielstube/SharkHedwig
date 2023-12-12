package Session;

import static Misc.Constants.*;

/**
 * This is an enum class to handle the multiple states of the protocol (State machine pattern). This is a clean methode to avoid
 * global variables, which are difficult to handle and which we want to avoid with a primary focus on a secure protocol.
 * Every state has three methods to set the next state, get the current state and check if a state is completed.
 * The different states are:
 * - NoState: This is the initial state when no message exchange is going on.
 *
 */
public enum SessionState {

    NoState {
        @Override
        public int getState() {
            return NO_SESSION;
        }

        @Override
        public int nextState() {
            return IDENTIFICATION_SESSION;
        }
        @Override
        public boolean stateCompleted() {
            return true;
        }
    },

    Identification {
        @Override
        public int getState() {
            return IDENTIFICATION_SESSION;
        }

        @Override
        public int nextState() {
            return REQUEST_SESSION;
        }
        @Override
        public boolean stateCompleted() {
            return true;
        }
    },

    Request {
        @Override
        public int getState() {
            return REQUEST_SESSION;
        }
        @Override
        public int nextState() {
            return CONTRACT_SESSION;
        }
        @Override
        public boolean stateCompleted() {
            return true;
        }
    },

    Handover() {
        @Override
        public int getState() {
            return CONTRACT_SESSION;
        }

        @Override
        public int nextState() {
            return NO_SESSION;
        }

        @Override
        public boolean stateCompleted() {
            return true;
        }
    };

    public abstract int getState();
    public abstract int nextState();

    public abstract boolean stateCompleted();
}
