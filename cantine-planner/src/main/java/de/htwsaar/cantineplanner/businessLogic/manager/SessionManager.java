package de.htwsaar.cantineplanner.businessLogic.manager;

import de.htwsaar.cantineplanner.codegen.tables.records.UsersRecord;

public class SessionManager {
    private UsersRecord currentUser;

    public int getCurrentUserId() {
        return currentUser.getUserid();
    }
    public void setCurrentUser(UsersRecord user) {
        this.currentUser = user;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public void logout() {
        currentUser = null;
    }
}