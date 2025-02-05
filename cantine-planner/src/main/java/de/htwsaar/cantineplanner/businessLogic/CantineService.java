// src/main/java/de/htwsaar/cantineplanner/businessLogic/CantineService.java
package de.htwsaar.cantineplanner.businessLogic;

import de.htwsaar.cantineplanner.codegen.tables.records.MealsRecord;
import de.htwsaar.cantineplanner.dataAccess.DBConnection;
import de.htwsaar.cantineplanner.exceptions.UserAlreadyExistsException;

import java.util.List;

public class CantineService {
    private DBConnection dbConnection;

    public CantineService() {
        this.dbConnection = new DBConnection();
    }

    public boolean validateUser(String username, String password) {
        return dbConnection.validateUser(username, password);
    }

   public boolean registerUser(String username, String password, String email) throws UserAlreadyExistsException {
        return dbConnection.registerUser(username, password, email);
    }

    public int getUserId(String username) {
        return dbConnection.getUserId(username);
    }

    public List<MealsRecord> getAllMeals() {
        return dbConnection.getAllMeals();
    }

    public List<String> getAllAllergies() {
        return dbConnection.getAllAllergies();
    }

    // Add other methods as needed
}