// src/main/java/de/htwsaar/cantineplanner/businessLogic/CantineService.java
package de.htwsaar.cantineplanner.businessLogic;

import de.htwsaar.cantineplanner.codegen.tables.records.MealsRecord;
import de.htwsaar.cantineplanner.codegen.tables.records.ReviewRecord;
import de.htwsaar.cantineplanner.codegen.tables.records.UsersRecord;
import de.htwsaar.cantineplanner.dataAccess.DBConnection;
import de.htwsaar.cantineplanner.exceptions.UserAlreadyExistsException;

import java.sql.SQLException;
import java.util.List;

public class CantineService {
    private DBConnection dbConnection;

    public CantineService(EventManager eventManager) {
        this.dbConnection = new DBConnection(eventManager);
    }

    public boolean validateUser(String username, String password) {
        return dbConnection.validateUser(username, password);
    }

   public boolean registerUser(String username, String password, String email) {
        UsersRecord usersRecord = dbConnection.registerUser(username, password, email);
        return usersRecord != null;
    }

    public int getUserId(String username) {
        return dbConnection.getUserId(username);
    }

    public List<MealsRecord> getAllMeals() {
        return dbConnection.getAllMeals();
    }

    public List<MealsRecord> getAllAllergies(){
        return dbConnection.getAllAllergies();
    }

    public List<ReviewRecord> getAllReviews(){
        return dbConnection.getAllReviews();
    }

    public MealsRecord addMeal(MealsRecord meal) {
        return dbConnection.addMeal(meal);
    }

    public void deleteMeal(int mealId) {
        dbConnection.deleteMeal(mealId);
    }

    public MealsRecord getMealById(int mealId) {
        List<MealsRecord> meals = dbConnection.mealDetails(mealId);
        return meals.isEmpty() ? null : meals.get(0);
    }

    public MealsRecord getMealByName(String name) {
        List<MealsRecord> meals = dbConnection.searchMeal(name);
        return meals.isEmpty() ? null : meals.get(0);
    }


    // Add other methods as needed
}