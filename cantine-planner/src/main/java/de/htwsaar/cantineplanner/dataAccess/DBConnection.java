package de.htwsaar.cantineplanner.dataAccess;

import de.htwsaar.cantineplanner.codegen.tables.Meals;
import de.htwsaar.cantineplanner.codegen.tables.Review;
import de.htwsaar.cantineplanner.codegen.tables.Users;
import de.htwsaar.cantineplanner.codegen.tables.records.MealsRecord;
import de.htwsaar.cantineplanner.codegen.tables.records.ReviewRecord;
import de.htwsaar.cantineplanner.codegen.tables.records.UsersRecord;
import de.htwsaar.cantineplanner.exceptions.*;
import de.htwsaar.cantineplanner.security.PasswordUtil;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public class DBConnection {
    private final HikariCPDataSource dataSource;


    /**
     * Constructor for DBConnection creates a new HikariCPDataSource
     */
    public DBConnection(String propertiesPath) {
        this.dataSource = new HikariCPDataSource(propertiesPath);

    }

    /**
     * Method getDSLContext returns a DSLContext object
     *
     * @param connection of type Connection
     * @return DSLContext
     */
    private DSLContext getDSLContext(Connection connection) {
        return DSL.using(connection, SQLDialect.SQLITE);
    }
    /**
     * Method setAllergyForUser sets allergies for a user by userId
     * @param userId of type int
     * @param allergies of type String
     * @return boolean true if the allergies are set, false otherwise
     * @throws SQLException if an SQL exception occurs
     */
    public boolean setAllergeneSettings(int userId, String allergies) throws SQLException {
      //  String cleanAllergies = allergies.replace("[", "").replace("]", "");
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            dsl.update(Users.USERS)
                    .set(Users.USERS.DONT_SHOW_MEAL, allergies)
                    .where(Users.USERS.USERID.eq(userId))
                    .execute();
            return true;
        }
    }
    /**
     * Method validateUser validates a user by username and password
     * @param username          of type String
     * @param plainTextPassword of type String
     * @throws UserNotValidatedException if the user is not validated
     */
    public boolean validateUser(String username, String plainTextPassword) throws SQLException, UserNotValidatedException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            String hashedPassword = dsl.select(Users.USERS.PASSWORD).from(Users.USERS).where(
                    Users.USERS.USERNAME.eq(username)).fetchOne(Users.USERS.PASSWORD);

            if (hashedPassword == null || !PasswordUtil.verifyPassword(plainTextPassword, hashedPassword)) {
                throw new UserNotValidatedException("Invalid username or password!");
            }
            return true;
        }
    }

    /**
     * Method getUserId searches for a user by username and returns the userId
     *
     * @param username of type String
     * @return userId of type int
     * @throws SQLException             if an SQL exception occurs
     * @throws UserDoesntExistException if the user doesn't exist
     */
    public int getUserId(String username) throws SQLException, UserDoesntExistException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);

            if (!dsl.fetchExists(dsl.selectFrom(Users.USERS).where(Users.USERS.USERNAME.eq(username)))) {
                throw new UserDoesntExistException("The user with the given username doesn't exist!");
            }

            return dsl.select(Users.USERS.USERID).from(Users.USERS).where(Users.USERS.USERNAME.eq(username)).fetchOne(
                    Users.USERS.USERID);
        }
    }

    /**
     * Method isAdmin checks if a user is an admin or not
     *
     * @return boolean true if the user is an admin, false otherwise
     * @throws SQLException             if an SQL exception occurs
     * @throws UserDoesntExistException if the user doesn't exist
     */
    public boolean isAdmin(int UserID) throws SQLException, UseriDDoesntExcistException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);

            if (!dsl.fetchExists(dsl.selectFrom(Users.USERS).where(Users.USERS.USERID.eq(UserID)))) {
                throw new UseriDDoesntExcistException("The user with the given username doesn't exist!");
            }

            return dsl.select(Users.USERS.ROLE).from(Users.USERS).where(Users.USERS.USERID.eq(UserID)).fetchOne(
                    Users.USERS.ROLE) == 1;
        }
    }

    /**
     * Method registerUser registers a user in the database with the given username, password and email
     *
     * @param username          of type String
     * @param plainTextPassword of type String
     * @param email             of type String
     * @return A UsersRecord of the registered user
     * @throws SQLException               if an SQL exception occurs
     * @throws UserAlreadyExistsException if the user already exists
     */
    public UsersRecord registerUser(String username, String plainTextPassword, String email) throws SQLException, UserAlreadyExistsException, InvalidEmailTypeException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);

            if (dsl.fetchExists(dsl.selectFrom(Users.USERS).where(Users.USERS.USERNAME.eq(username)))) {
                throw new UserAlreadyExistsException("Username already exists, please choose another one!");
            }

            if (email != null && !isValidEmail(email)) {
                throw new InvalidEmailTypeException("Invalid email type!");
            }
            String hashedPassword = PasswordUtil.hashPassword(plainTextPassword);
            dsl.insertInto(Users.USERS).set(Users.USERS.USERNAME, username).set(Users.USERS.PASSWORD,
                    hashedPassword).set(Users.USERS.EMAIL, email).set(Users.USERS.ROLE, 0).execute();

            return dsl.selectFrom(Users.USERS).where(Users.USERS.USERNAME.eq(username)).fetchOne();
        }
    }

    /**
     * Method isValidEmail checks if the email is valid
     *
     * @param email of type String
     * @return boolean true if the email is valid, false otherwise
     */
    public boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    /**
     * Method deleteUserById deletes a user from the database by userId
     *
     * @param userId of type int of the user to be deleted
     * @throws SQLException             if an SQL exception occurs
     * @throws UserDoesntExistException if the user doesn't exist
     */
    public void deleteUserById(int userId) throws SQLException, UserDoesntExistException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);

            if (!dsl.fetchExists(dsl.selectFrom(Users.USERS).where(Users.USERS.USERID.eq(userId)))) {
                throw new UserDoesntExistException("The user with the given ID doesn't exist!");
            }

            dsl.deleteFrom(Users.USERS).where(Users.USERS.USERID.eq(userId)).execute();
        }
    }

    /**
     * Method deleteUserByName deletes a user from the database by username
     *
     * @param UserName of type String of the user to be deleted
     * @throws SQLException             if an SQL exception occurs
     * @throws UserDoesntExistException if the user doesn't exist
     */
    public void deleteUserByName(String UserName) throws SQLException, UserDoesntExistException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);

            if (!dsl.fetchExists(dsl.selectFrom(Users.USERS).where(Users.USERS.USERNAME.eq(UserName)))) {
                throw new UserDoesntExistException("The user with the given username doesn't exist!");
            }

            dsl.deleteFrom(Users.USERS).where(Users.USERS.USERNAME.eq(UserName)).execute();
        }
    }

    /**
     * Method addMeal adds a meal for the database
     *
     * @param meal of type MealsRecord to be added
     */
    public MealsRecord addMeal(MealsRecord meal) throws SQLException, MealAlreadyExistsException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            if (dsl.fetchExists(dsl.selectFrom(Meals.MEALS).where(Meals.MEALS.NAME.eq(meal.getName())))) {
                throw new MealAlreadyExistsException(" Meal Already exists !");
            }

            dsl.insertInto(Meals.MEALS).set(Meals.MEALS.NAME, meal.getName()).set(Meals.MEALS.PRICE,
                    meal.getPrice()).set(Meals.MEALS.CALORIES, meal.getCalories()).set(Meals.MEALS.ALLERGY,
                    meal.getAllergy()).set(Meals.MEALS.MEAT, meal.getMeat()).set(Meals.MEALS.DAY,
                    meal.getDay()).execute();

            return dsl.selectFrom(Meals.MEALS).where(Meals.MEALS.NAME.eq(meal.getName())).fetchOne();

        }

    }

    /**
     * Method getAllMeals displays all meals in the database
     */
    public List<MealsRecord> getAllMeals() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            return dsl.selectFrom(Meals.MEALS).fetchInto(MealsRecord.class);
        }
    }

    /**
     * Method getAllAllergies displays all allergies in the database
     */
    public List<MealsRecord> getAllAllergies() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            return dsl.selectFrom(Meals.MEALS).fetchInto(MealsRecord.class);
        }
    }

    /**
     * Method deleteMeal deletes a meal from the database
     *
     * @param mealId of type int of the meal to be deleted
     */
    public void deleteMealById(int mealId) throws SQLException, MealiDNotFoundException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            if (!dsl.fetchExists(dsl.selectFrom(Meals.MEALS).where(Meals.MEALS.MEAL_ID.eq(mealId)))) {
                throw new MealiDNotFoundException("The meal with the given ID doesn't exist!");
            }

            dsl.deleteFrom(Meals.MEALS).where(Meals.MEALS.MEAL_ID.eq(mealId)).execute();
        }
    }

    /**
     * Method searchMeal searches for a meal by name
     *
     * @param name of type String of the meal to be searched
     */
    public List<MealsRecord> searchMealByName(String name) throws SQLException, MealDoesntExistException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            if (!dsl.fetchExists(dsl.selectFrom(Meals.MEALS).where(Meals.MEALS.NAME.eq(name)))) {
                throw new MealDoesntExistException("Meal with name " + name + " doesnt exist !");
            }

            return dsl.selectFrom(Meals.MEALS).where(Meals.MEALS.NAME.eq(name)).fetchInto(MealsRecord.class);
        }
    }

    /**
     * Method mealDetails displays the details of a meal
     *
     * @param mealId of type int of the meal to be displayed
     */
    public List<MealsRecord> searchMealById(int mealId) throws SQLException, MealiDNotFoundException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            if (!dsl.fetchExists(dsl.selectFrom(Meals.MEALS).where(Meals.MEALS.MEAL_ID.eq(mealId)))) {
                throw new MealiDNotFoundException("Meal with the given iD " + mealId + " doesnt exist !");
            }
            return dsl.selectFrom(Meals.MEALS).where(Meals.MEALS.MEAL_ID.eq(mealId)).fetchInto(MealsRecord.class);
        }
    }

    /**
     * Method getAllReviews displays all reviews in the database
     */
    public List<ReviewRecord> getAllReviews() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            return dsl.selectFrom(Review.REVIEW).fetchInto(ReviewRecord.class);
        }
    }

    /**
     * Method getAllReviews displays all reviews in the database
     */
    public List<ReviewRecord> getAllReviewsByUser(int userId) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            return dsl.selectFrom(Review.REVIEW).where(Review.REVIEW.USERID.eq(userId)).fetchInto(ReviewRecord.class);
        }
    }


    /**
     * Method reviewsByMealName searches for reviews by meal name
     *
     * @param mealName of type String of the meal to be searched
     */
    public List<ReviewRecord> reviewsByMealName(String mealName) throws SQLException, MealDoesntExistException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            if (!dsl.fetchExists(dsl.selectFrom(Meals.MEALS).where(Meals.MEALS.NAME.eq(mealName)))) {
                throw new MealDoesntExistException("Meal with name" + mealName + " doesnt exist !");
            }
            return dsl.select().from(Review.REVIEW).join(Meals.MEALS).on(
                    Review.REVIEW.MEAL_ID.eq(Meals.MEALS.MEAL_ID)).where(Meals.MEALS.NAME.eq(mealName)).fetchInto(
                    ReviewRecord.class);
        }
    }


    /**
     * Method deleteReview deletes a review from the database
     *
     * @param ratingId of type int of the review to be deleted
     */
    public void deleteReview(int ratingId) throws SQLException, ReviewiDDoesntExistException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            // if hte review iD doesnt exist, we throw an exception
            if (!dsl.fetchExists(dsl.selectFrom(Review.REVIEW).where(Review.REVIEW.RATING_ID.eq(ratingId)))) {
                throw new ReviewiDDoesntExistException("Review iD that was provided  does not exist ! ");
            }
            // deletion of the corresponding review iD
            dsl.deleteFrom(Review.REVIEW).where(Review.REVIEW.RATING_ID.eq(ratingId)).execute();
        }

    }

    /**
     * Method addReview adds a review for the database
     *
     * @param givenReview of type ReviewRecord to be added
     */
    public boolean addReview(ReviewRecord givenReview) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            dsl.insertInto(Review.REVIEW).set(Review.REVIEW.MEAL_ID, givenReview.getMealId()).set(Review.REVIEW.RATING,
                    givenReview.getRating()).set(Review.REVIEW.COMMENT, givenReview.getComment()).set(
                    Review.REVIEW.USERID, givenReview.getUserid()).execute();
            return true;
        }
    }

    /**
     * Method getWeeklyPlan returns the weekly plan of meals
     *
     * @return List of MealsRecord
     * @throws SQLException if an SQL exception occurs
     */
    public List<MealsRecord> getWeeklyPlan() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            return dsl.selectFrom(Meals.MEALS).where(Meals.MEALS.DAY.isNotNull()).fetchInto(MealsRecord.class);

        }
    }

    /**
     * Method editWeeklyPlan edits the weekly plan
     *
     * @param mealName of type String
     * @param day      of type String
     * @throws SQLException             if an SQL exception occurs
     * @throws MealDoesntExistException if the meal doesn't exist
     */
    public void editWeeklyPlan(String mealName, String day) throws SQLException, MealDoesntExistException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext create = DSL.using(connection, SQLDialect.SQLITE);
            int rowsAffected = create.update(DSL.table("meals")).set(DSL.field("day"), day).where(
                    DSL.field("Name").eq(mealName)).execute();
            if (rowsAffected == 0) {
                throw new MealDoesntExistException("Meal does not exist");
            }
        } catch (SQLException e) {
            throw new SQLException("Error updating weekly plan", e);
        }
    }

    /**
     * Method resetWeeklyPlan resets the weekly plan
     *
     * @throws SQLException if an SQL exception occurs
     */
    public void resetWeeklyPlan() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            dsl.update(DSL.table("meals")).set(DSL.field("day"), (String) null).execute();
        }
    }

    /**
     * Method editUserData edits the user data in the database
     *
     * @param currentUserId of type int
     * @param newPassword   of type String
     * @param newEmail      of type String
     * @throws SQLException             if an SQL exception occurs
     * @throws UserDoesntExistException if the user doesn't exist
     */
    public void editUserData(int currentUserId, String newPassword, String newEmail) throws SQLException, InvalidEmailTypeException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);

            if (newEmail != null && !isValidEmail(newEmail)) {
                throw new InvalidEmailTypeException("Invalid email type!");
            }

            // Update the user's password if provided
            if (newPassword != null && !newPassword.isEmpty()) {
                String hashedPassword = PasswordUtil.hashPassword(newPassword);
                dsl.update(DSL.table("users")).set(DSL.field("password"), hashedPassword).where(
                        DSL.field("userid").eq(currentUserId)).execute();
            }

            // Update the user's email if provided
            if (newEmail != null && !newEmail.isEmpty()) {
                dsl.update(DSL.table("users")).set(DSL.field("email"), newEmail).where(
                        DSL.field("userid").eq(currentUserId)).execute();
            }
        }
    }
}