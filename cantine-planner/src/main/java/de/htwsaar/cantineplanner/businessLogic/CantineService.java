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
    private final DBConnection dbConnection;


    public CantineService(String propertiesFilePath) {
        this.dbConnection = new DBConnection(propertiesFilePath);
    }

    public boolean validateUser(String username, String password) throws SQLException, UserNotValidatedException {
        return dbConnection.validateUser(username, password);
    }
    public boolean validateUser(int userID, String password) throws SQLException, UserNotValidatedException {
        return dbConnection.validateUser(userID, password);
    }

    public boolean registerUser(String username, String password, String email) throws SQLException, UserAlreadyExistsException, InvalidEmailTypeException {
        UsersRecord usersRecord = dbConnection.registerUser(username, password, email);
        return usersRecord != null;
    }

    public void editMeal(MealsRecord meal) throws SQLException, MealDoesntExistException {
        dbConnection.editMeal(meal);
    }
    public int getUserId(String username) throws SQLException, UserDoesntExistException {
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

    public void setAllergeneSettings(int userId, String allergene) throws SQLException, UserDoesntExistException {
        allergene = allergene.replaceAll("^\\[|\\]$", "");
        String[] allergeneArray = allergene.split(",");
        StringBuilder abbrAllergene = new StringBuilder();
        for (String allergen : allergeneArray) {
            String trimmedAllergen = allergen.trim();
            String abbr = AllergenMapper.getAllergenCode(trimmedAllergen);
            if (abbr != null) {
                if (abbrAllergene.length() > 0) {
                    abbrAllergene.append(",");
                }
                abbrAllergene.append(abbr); // Append the abbreviation
            }
        }
        dbConnection.setAllergeneSettings(userId, abbrAllergene.toString());
    }

    public void deleteMeal(int mealId) throws SQLException, MealiDNotFoundException {
        dbConnection.deleteMealById(mealId);
    }

    public MealsRecord getMealById(int mealId) throws SQLException, MealiDNotFoundException {
        List<MealsRecord> meals = dbConnection.searchMealById(mealId);
        return meals.isEmpty() ? null : meals.get(0);
    }

    public MealsRecord getMealByName(String name) throws SQLException, MealDoesntExistException {
        List<MealsRecord> meals = dbConnection.searchMealByName(name);
        return meals.isEmpty() ? null : meals.get(0);
    }

    public void addReview(ReviewRecord review) throws SQLException {
        dbConnection.addReview(review);
    }

    public List<ReviewRecord> searchReviewsByMealName(String mealName) throws SQLException, MealDoesntExistException {
        return dbConnection.reviewsByMealName(mealName);
    }

    public List<ReviewRecord> getAllReviewsByUser(int currentUserId) throws SQLException {
        return dbConnection.getAllReviewsByUser(currentUserId);
    }
    public void updateUserRole(int userId, int role) throws SQLException, UserNotValidatedException {
        dbConnection.updateUserRole(userId, role);
    }

    public boolean isAdmin(int currentUserId) throws SQLException, UseriDDoesntExcistException {
        return dbConnection.isAdmin(currentUserId);
    }

    public List<MealsRecord> getWeeklyPlan() throws SQLException {
        return dbConnection.getWeeklyPlan();
    }

    public void editWeeklyPlan(String mealName, String day) throws SQLException, MealDoesntExistException {
        dbConnection.editWeeklyPlan(mealName, day);
    }

    public void resetWeeklyPlan() throws SQLException {
        dbConnection.resetWeeklyPlan();
    }

    public void editUserData(int currentUserId, String newPassword, String newEmail) throws SQLException, InvalidEmailTypeException {
        dbConnection.editUserData(currentUserId, newPassword, newEmail);
    }
    public List<UsersRecord> getAllUser() throws SQLException {
        return dbConnection.getAllUser();
    }
    public void deleteUser(int userId) throws SQLException, UserDoesntExistException {
        dbConnection.deleteUserById(userId);
    }
}