package de.htwsaar.cantineplanner.businessLogic.manager;

    import de.htwsaar.cantineplanner.codegen.tables.records.UsersRecord;

    /**
     * Manages the session for the current user.
     */
    public class SessionManager {
        private UsersRecord currentUser;

        /**
         * Gets the user ID of the current user.
         *
         * @return the user ID of the current user
         */
        public int getCurrentUserId() {
            return currentUser.getUserid();
        }

        /**
         * Sets the current user.
         *
         * @param user the user to set as the current user
         */
        public void setCurrentUser(UsersRecord user) {
            this.currentUser = user;
        }

        /**
         * Checks if a user is logged in.
         *
         * @return true if a user is logged in, false otherwise
         */
        public boolean isLoggedIn() {
            return currentUser != null;
        }

        /**
         * Logs out the current user.
         */
        public void logout() {
            currentUser = null;
        }
    }