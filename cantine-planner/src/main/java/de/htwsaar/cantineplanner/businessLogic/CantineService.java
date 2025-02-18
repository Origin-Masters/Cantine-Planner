// src/main/java/de/htwsaar/cantineplanner/businessLogic/CantineService.java
package de.htwsaar.cantineplanner.businessLogic;

import de.htwsaar.cantineplanner.codegen.tables.records.MealsRecord;
import de.htwsaar.cantineplanner.codegen.tables.records.ReviewRecord;
import de.htwsaar.cantineplanner.codegen.tables.records.UsersRecord;
import de.htwsaar.cantineplanner.dataAccess.DBConnection;
import de.htwsaar.cantineplanner.exceptions.*;

import java.sql.SQLException;
import java.util.List;

public class CantineService {
    private DBConnection dbConnection;



    public CantineService(String propertiesFilePath) {
        this.dbConnection = new DBConnection(propertiesFilePath);
    }

    public boolean validateUser(String username, String password) throws SQLException {
        return dbConnection.validateUser(username, password);
    }

   public boolean registerUser(String username, String password, String email) throws SQLException, UserAlreadyExistsException {
        UsersRecord usersRecord = dbConnection.registerUser(username, password, email);
        return usersRecord != null;
    }

    public int getUserId(String username) throws SQLException, NullPointerException {
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

    public MealsRecord addMeal(MealsRecord meal) throws SQLException, MealAlreadyExistsException {
        return dbConnection.addMeal(meal);
    }
    public void deleteReview(int reviewId) throws SQLException, ReviewiDDoesntExistException {
        dbConnection.deleteReview(reviewId);
    }


    public void deleteMeal(int mealId) throws SQLException , MealDoesntExistException {
        dbConnection.deleteMealById(mealId);
    }

    public MealsRecord getMealById(int mealId) throws SQLException , MealDoesntExistException {
        List<MealsRecord> meals = dbConnection.mealDetails(mealId);
        return meals.isEmpty() ? null : meals.get(0);
    }

    public MealsRecord getMealByName(String name) throws SQLException {
        List<MealsRecord> meals = dbConnection.searchMeal(name);
        return meals.isEmpty() ? null : meals.get(0);
    }

    public void addReview(ReviewRecord review) throws SQLException {
        dbConnection.addReview(review);
    }

    public List<ReviewRecord> searchReviewsByMealId(int mealId) throws SQLException {
        return dbConnection.reviewsByMealiD(mealId);
    }

    public List<ReviewRecord> searchReviewsByMealName(String mealName) throws SQLException {
        return dbConnection.reviewsByMealName(mealName);
    }

    public List<ReviewRecord> getAllReviewsByUser(int currentUserId) throws SQLException {
        return dbConnection.getAllReviewsByUser(currentUserId);
    }

    public boolean isAdmin(int currentUserId) throws SQLException, UserDoesntExistException, NullPointerException {
        return dbConnection.isAdmin(currentUserId);
    }

    public List<MealsRecord> getWeeklyPlan() throws SQLException {
        return dbConnection.getWeeklyPlan();
    }

    public void editWeeklyPlan(String mealName, String day) throws SQLException, MealDoesntExistException {
        dbConnection.editWeeklyPlan(mealName, day);
    }

    public  void resetWeeklyPlan() throws SQLException {
        dbConnection.resetWeeklyPlan();
    }

    public void editUserData(int currentUserId, String newPassword, String newEmail) throws SQLException {
        dbConnection.editUserData(currentUserId, newPassword, newEmail);
    }
    // Add other methods as needed
}