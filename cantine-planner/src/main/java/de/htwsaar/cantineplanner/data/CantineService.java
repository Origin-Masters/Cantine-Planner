// Java
package de.htwsaar.cantineplanner.data;

import com.zaxxer.hikari.HikariConfig;
import de.htwsaar.cantineplanner.businessLogic.AllergenMapper;
import de.htwsaar.cantineplanner.codegen.tables.records.MealsRecord;
import de.htwsaar.cantineplanner.codegen.tables.records.ReviewRecord;
import de.htwsaar.cantineplanner.codegen.tables.records.UsersRecord;

import de.htwsaar.cantineplanner.dataAccess.HikariCPDataSource;
import de.htwsaar.cantineplanner.exceptions.*;

import java.sql.SQLException;
import java.util.List;

/**
 * Provides service-level operations for the cantine planner application.
 * Groups user, meal, review, allergene, and weekly plan operations.
 */
public class CantineService {
   // private final DBConnection dbConnection;

    MealsRepository mealsRepository;
    ReviewRepository reviewRepository;
    UserRepository userRepository;
    WeeklyRepository weeklyRepository;
    HikariCPDataSource hikariCPDataSource;

    //AbstractRepository abstractRepository;

    ////////////////////////////////////////////////////////////////////////////////
    // Constructor
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Initializes the CantineService with the specified properties file.
     *
     * @param propertiesFilePath path to the database configuration properties file
     */
    public CantineService(String propertiesFilePath) {

        hikariCPDataSource = new HikariCPDataSource(propertiesFilePath);

       // this.dbConnection = new DBConnection(propertiesFilePath);

        this.mealsRepository = new MealsRepository(hikariCPDataSource);
        this.reviewRepository = new ReviewRepository(hikariCPDataSource);
        this.userRepository = new UserRepository(hikariCPDataSource);
        this.weeklyRepository = new WeeklyRepository(hikariCPDataSource);


    }

    ////////////////////////////////////////////////////////////////////////////////
    // User Validation and Registration
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Validates a user by username and password.
     *
     * @param username user name
     * @param password user password
     * @return true if validation succeeds, otherwise false
     * @throws SQLException              if a database error occurs
     * @throws UserNotValidatedException if the user could not be validated
     */
    public boolean validateUser(String username, String password) throws SQLException, UserNotValidatedException {
        return userRepository.validateUser(username, password);
    }

    /**
     * Validates a user by user ID and password.
     *
     * @param userID   user ID
     * @param password user password
     * @return true if validation succeeds, otherwise false
     * @throws SQLException              if a database error occurs
     * @throws UserNotValidatedException if the user could not be validated
     */
    public boolean validateUser(int userID, String password) throws SQLException, UserNotValidatedException {
        return userRepository.validateUser(userID, password);
    }

    /**
     * Registers a new user with username, password, and email.
     *
     * @param username user's name
     * @param password user's password
     * @param email    user's email
     * @return true if the user was registered, otherwise false
     * @throws SQLException               if a database error occurs
     * @throws UserAlreadyExistsException if the user already exists
     * @throws InvalidEmailTypeException  if an email format is invalid
     */
    public boolean registerUser(String username, String password, String email)
            throws SQLException, UserAlreadyExistsException, InvalidEmailTypeException {
        UsersRecord usersRecord = userRepository.registerUser(username, password, email);
        return usersRecord != null;
    }

    /**
     * Retrieves the user ID for the specified username.
     *
     * @param username user's name
     * @return user ID
     * @throws SQLException             if a database error occurs
     * @throws UserDoesntExistException if the user does not exist
     */
    public int getUserId(String username) throws SQLException, UserDoesntExistException {
        return userRepository.getUserId(username);
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Meal Operations
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Edits an existing meal.
     *
     * @param meal the meal record to edit
     * @throws SQLException             if a database error occurs
     * @throws MealDoesntExistException if the meal does not exist
     */
    public void editMeal(MealsRecord meal) throws SQLException, MealDoesntExistException {
        mealsRepository.editMeal(meal);
    }

    /**
     * Returns a list of all meals.
     *
     * @return list of meals
     * @throws SQLException if a database error occurs
     */
    public List<MealsRecord> getAllMeals() throws SQLException {
        return mealsRepository.getAllMeals();
    }

    /**
     * Returns a list of all meals showing allergy info.
     *
     * @return list of meals with allergen data
     * @throws SQLException if a database error occurs
     */
    public List<MealsRecord> getAllAllergies() throws SQLException {
        return mealsRepository.getAllAllergies();
    }

    /**
     * Adds a new meal.
     *
     * @param meal meal record to add
     * @throws SQLException               if a database error occurs
     * @throws MealAlreadyExistsException if the meal already exists
     */
    public void addMeal(MealsRecord meal) throws SQLException, MealAlreadyExistsException {
        mealsRepository.addMeal(meal);
    }

    /**
     * Deletes a meal by meal ID.
     *
     * @param mealId meal ID
     * @throws SQLException            if a database error occurs
     * @throws MealiDNotFoundException if the meal does not exist
     */
    public void deleteMeal(int mealId) throws SQLException, MealiDNotFoundException {
        mealsRepository.deleteMealById(mealId);
    }

    /**
     * Retrieves a meal by ID.
     *
     * @param mealId meal ID
     * @return meal record or null if not found
     * @throws SQLException            if a database error occurs
     * @throws MealiDNotFoundException if the meal ID is invalid
     */
    public MealsRecord getMealById(int mealId) throws SQLException, MealiDNotFoundException {
        List<MealsRecord> meals = mealsRepository.searchMealById(mealId);
        return meals.isEmpty() ? null : meals.get(0);
    }

    /**
     * Retrieves a meal by name.
     *
     * @param name meal name
     * @return meal record or null if not found
     * @throws SQLException             if a database error occurs
     * @throws MealDoesntExistException if the meal name is invalid
     */
    public MealsRecord getMealByName(String name) throws SQLException, MealDoesntExistException {
        List<MealsRecord> meals = mealsRepository.searchMealByName(name);
        return meals.isEmpty() ? null : meals.get(0);
    }


    /**
     * Sorts meals by price in ascending order.
     *
     * @return a list of meals sorted by price
     * @throws SQLException if a database error occurs
     */
    public List<MealsRecord> sortMealsByPrice() throws SQLException {
        return mealsRepository.sortMealsByPrice();
    }

    /**
     * Sorts meals by rating in descending order.
     *
     * @return a list of meals sorted by rating
     * @throws SQLException if a database error occurs
     */
    public List<MealsRecord> sortMealsByRating() throws SQLException {
        return mealsRepository.sortMealsByRating();
    }

    /**
     * Sorts meals by name in alphabetical order.
     *
     * @return a list of meals sorted by name
     * @throws SQLException if a database error occurs
     */
    public List<MealsRecord> sortMealsByName() throws SQLException {
        return mealsRepository.sortMealsByName();
    }

    /**
     * Sorts meals by calories in ascending order.
     *
     * @return a list of meals sorted by calories
     * @throws SQLException if a database error occurs
     */
    public List<MealsRecord> sortMealsByCalories() throws SQLException {
        return mealsRepository.sortMealsByCalories();
    }

    /**
     * Sorts meals by excluding those that contain the user's allergies.
     *
     * @param currentUserId the ID of the current user
     * @return a list of meals excluding those with the user's allergies
     * @throws SQLException if a database error occurs
     */
    public List<MealsRecord> sortMealsByAllergy(int currentUserId) throws SQLException {
        return mealsRepository.sortMealsByAllergy(currentUserId);
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Review Operations
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns all reviews.
     *
     * @return list of review records
     * @throws SQLException if a database error occurs
     */
    public List<ReviewRecord> getAllReviews() throws SQLException {
        return reviewRepository.getAllReviews();
    }

    /**
     * Returns the user ID from a review ID.
     *
     * @param reviewId review ID
     * @return user ID
     * @throws SQLException                 if a database error occurs
     * @throws ReviewiDDoesntExistException if the review ID does not exist
     */
    public int getUserIdFromReviewId(int reviewId) throws SQLException, ReviewiDDoesntExistException {
        return reviewRepository.getUserIdFromReviewId(reviewId);
    }

    /**
     * Adds a new review.
     *
     * @param review the review record to add
     * @throws SQLException if a database error occurs
     */
    public void addReview(ReviewRecord review) throws SQLException {
        reviewRepository.addReview(review);
    }

    /**
     * Searches reviews by meal name.
     *
     * @param mealName the meal name
     * @return list of matching reviews
     * @throws SQLException             if a database error occurs
     * @throws MealDoesntExistException if the meal does not exist
     */
    public List<ReviewRecord> searchReviewsByMealName(String mealName) throws SQLException, MealDoesntExistException {
        return reviewRepository.reviewsByMealName(mealName);
    }

    /**
     * Returns all reviews by a specific user.
     *
     * @param currentUserId user ID
     * @return list of relevant reviews
     * @throws SQLException if a database error occurs
     */
    public List<ReviewRecord> getAllReviewsByUser(int currentUserId) throws SQLException {
        return reviewRepository.getAllReviewsByUser(currentUserId);
    }

    /**
     * Deletes a review by review ID.
     *
     * @param reviewId review ID
     * @throws SQLException                 if a database error occurs
     * @throws ReviewiDDoesntExistException if the review does not exist
     */
    public void deleteReview(int reviewId) throws SQLException, ReviewiDDoesntExistException {
        reviewRepository.deleteReview(reviewId);
    }

    /**
     * Calculates the median rating for a given meal.
     *
     * @param mealId the ID of the meal for which to calculate the median rating
     * @return the median rating of the meal
     * @throws SQLException if a database error occurs
     */
    public double calculateMedianRatingForMeal(int mealId) throws SQLException {
        return mealsRepository.calculateMedianRatingForMeal(mealId);
    }


    ////////////////////////////////////////////////////////////////////////////////
    // Allergene Settings
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Sets allergene settings for a user.
     *
     * @param userId    user ID
     * @param allergene allergenes in string form
     * @throws SQLException             if a database error occurs
     * @throws UserDoesntExistException if the user does not exist
     */
    public void setAllergeneSettings(int userId, String allergene) throws SQLException, UserDoesntExistException {
        System.out.println(allergene);
        allergene = allergene.replaceAll("^\\[|]$", "");
        String[] allergeneArray = allergene.split(",");
        StringBuilder abbrAllergene = new StringBuilder();
        for (String allergen : allergeneArray) {
            String trimmedAllergen = allergen.trim();
            String abbr = AllergenMapper.getAllergenCode(trimmedAllergen);
            if (abbr != null) {
                if (!abbrAllergene.isEmpty()) {
                    abbrAllergene.append(",");
                }
                abbrAllergene.append(abbr);
            }
        }
        System.out.println(abbrAllergene + " " + userId);
        userRepository.setAllergeneSettings(userId, abbrAllergene.toString());
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Weekly Plan
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns the weekly meal plan.
     *
     * @return list of main meal records for the current weekly plan
     * @throws SQLException if a database error occurs
     */
    public List<MealsRecord> getWeeklyPlan() throws SQLException {
        return weeklyRepository.getWeeklyPlan();
    }

    /**
     * Edits a weekly plan entry for a given day with a given meal name.
     *
     * @param mealName meal name
     * @param day      shorthand day (Mon, Tue, etc.)
     * @throws SQLException             if a database error occurs
     * @throws MealDoesntExistException if the meal does not exist
     */
    public void editWeeklyPlan(String mealName, String day) throws SQLException, MealDoesntExistException {
        weeklyRepository.editWeeklyPlan(mealName, day);
    }

    /**
     * Resets the current weekly plan.
     *
     * @throws SQLException if a database error occurs
     */
    public void resetWeeklyPlan() throws SQLException {
        weeklyRepository.resetWeeklyPlan();
    }

    ////////////////////////////////////////////////////////////////////////////////
    // User Data Management
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Edits user data (password, email).
     *
     * @param currentUserId user ID
     * @param newPassword   new password
     * @param newEmail      new email
     * @throws SQLException              if a database error occurs
     * @throws InvalidEmailTypeException if the new email is invalid
     */
    public void editUserData(int currentUserId, String newPassword, String newEmail)
            throws SQLException, InvalidEmailTypeException {
        userRepository.editUserData(currentUserId, newPassword, newEmail);
    }

    /**
     * Retrieves all users.
     *
     * @return list of user records
     * @throws SQLException if a database error occurs
     */
    public List<UsersRecord> getAllUser() throws SQLException {
        return userRepository.getAllUser();
    }

    /**
     * Deletes a user by user ID.
     *
     * @param userId user ID
     * @throws SQLException             if a database error occurs
     * @throws UserDoesntExistException if the specified user does not exist
     */
    public void deleteUser(int userId) throws SQLException, UserDoesntExistException {
        userRepository.deleteUserById(userId);
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Role Management
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Updates the role for a given user ID.
     *
     * @param userId user ID
     * @param role   new role to set
     * @throws SQLException              if a database error occurs
     * @throws UserNotValidatedException if the user couldn't be validated
     */
    public void updateUserRole(int userId, int role) throws SQLException, UserNotValidatedException {
        userRepository.updateUserRole(userId, role);
    }

    /**
     * Checks if the specified user is an admin.
     *
     * @param currentUserId user ID
     * @return true if admin, otherwise false
     * @throws SQLException                if a database error occurs
     * @throws UseriDDoesntExcistException if the user does not exist
     */
    public boolean isAdmin(int currentUserId) throws SQLException, UseriDDoesntExcistException {
        return userRepository.isAdmin(currentUserId);
    }


    public UsersRecord getUser(String username) throws SQLException , UserDoesntExistException{
        return userRepository.getUser(username);
    }
}