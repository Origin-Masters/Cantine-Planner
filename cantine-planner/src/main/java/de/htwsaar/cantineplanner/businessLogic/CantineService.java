// src/main/java/de/htwsaar/cantineplanner/businessLogic/CantineService.java
package de.htwsaar.cantineplanner.businessLogic;

import de.htwsaar.cantineplanner.dataAccess.DBConnection;

public class CantineService {
    private DBConnection dbConnection;

    public CantineService() {
        this.dbConnection = new DBConnection();
    }

    public boolean validateUser(String username, String password) {
        return dbConnection.validateUser(username, password);
    }

   public boolean registerUser(String username, String password, String email) {
        return dbConnection.registerUser(username, password, email);
    }

    // Add other methods as needed
}