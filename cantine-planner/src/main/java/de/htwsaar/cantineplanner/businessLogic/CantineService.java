// src/main/java/de/htwsaar/cantineplanner/businessLogic/CantineService.java
package de.htwsaar.cantineplanner.businessLogic;

import de.htwsaar.cantineplanner.codegen.tables.records.MealsRecord;
import de.htwsaar.cantineplanner.codegen.tables.records.ReviewRecord;
import de.htwsaar.cantineplanner.dataAccess.DBConnection;
import de.htwsaar.cantineplanner.exceptions.UserAlreadyExistsException;

import java.sql.SQLException;
import java.util.List;

public class CantineService {
    private DBConnection dbConnection;

    public CantineService(EventManager ) {
        this.dbConnection = new DBConnection(this.eventManager);
    }

    public boolean validateUser(String username, String password) throws SQLException {
        return dbConnection.validateUser(username, password);
    }

   public boolean registerUser(String username, String password, String email) {
        return dbConnection.registerUser(username, password, email);
    }

    public int getUserId(String username) {
        return dbConnection.getUserId(username);
    }

    public List<MealsRecord> getAllMeals() throws SQLException {
        return dbConnection.getAllMeals();
    }

    public List<MealsRecord> getAllAllergies() throws SQLException {
        return dbConnection.getAllAllergies();
    }

    public List<ReviewRecord> getAllReviews() throws SQLException {
        return dbConnection.getAllReviews();
    }

    // Add other methods as needed
}