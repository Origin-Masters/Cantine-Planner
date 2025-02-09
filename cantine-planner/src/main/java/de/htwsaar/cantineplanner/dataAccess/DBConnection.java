package de.htwsaar.cantineplanner.dataAccess;

import de.htwsaar.cantineplanner.businessLogic.AllergenMapper;
import de.htwsaar.cantineplanner.businessLogic.EventManager;
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
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;



public class DBConnection {
    private final HikariCPDataSource dataSource;

    /**
     * Constructor for DBConnection creates a new HikariCPDataSource
     */
    public DBConnection() {
        this.dataSource = new HikariCPDataSource();

    }

    /**
     * Method getDSLContext returns a DSLContext object
     *
     * @param connection of type Connection
     * @return DSLContext
     */
    private DSLContext getDSLContext(Connection connection) {
        return DSL.using(connection, SQLDialect.MYSQL);
    }

    /**
     * Method validateUser validates a user
     *
     * @param username          of type String
     * @param plainTextPassword of type String
     * @throws UserNotValidatedException if the user is not validated
     */
    public boolean validateUser(String username, String plainTextPassword) throws SQLException, UserNotValidatedException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            String hashedPassword = dsl.select(Users.USERS.PASSWORD)
                    .from(Users.USERS)
                    .where(Users.USERS.USERNAME.eq(username))
                    .fetchOne(Users.USERS.PASSWORD);

            if (hashedPassword == null || !PasswordUtil.verifyPassword(plainTextPassword, hashedPassword)) {
                throw new UserNotValidatedException("Invalid username or password!");
            }
            return true;
        }
    }

    // DBConnection.java
    public int getUserId(String username) throws SQLException, UserDoesntExistException, NullPointerException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);

            if (!dsl.fetchExists(
                    dsl.selectFrom(Users.USERS)
                            .where(Users.USERS.USERNAME.eq(username)))) {
                throw new UserDoesntExistException("The user with the given username doesn't exist!");
            }

            return dsl.select(Users.USERS.USERID)
                    .from(Users.USERS)
                    .where(Users.USERS.USERNAME.eq(username))
                    .fetchOne(Users.USERS.USERID);
        }
    }

    public UsersRecord registerUser(String username, String plainTextPassword, String email) throws SQLException, UserAlreadyExistsException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);

            if (dsl.fetchExists(
                    dsl.selectFrom(Users.USERS)
                            .where(Users.USERS.USERNAME.eq(username)))) {
                throw new UserAlreadyExistsException("Username already exists, please choose another one!");
            }

            String hashedPassword = PasswordUtil.hashPassword(plainTextPassword);
            dsl.insertInto(Users.USERS)
                    .set(Users.USERS.USERNAME, username)
                    .set(Users.USERS.PASSWORD, hashedPassword)
                    .set(Users.USERS.EMAIL, email)
                    .set(Users.USERS.ROLE, 0)
                    .execute();

            return dsl.selectFrom(Users.USERS)
                    .where(Users.USERS.USERNAME.eq(username))
                    .fetchOne();
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
            if (dsl.fetchExists(
                            dsl.selectFrom(Meals.MEALS)
                                    .where(Meals.MEALS.NAME.eq(meal.getName())))
            ) {
                throw new MealAlreadyExistsException(" Meal Already exists !");
            }

            dsl.insertInto(Meals.MEALS)
                    .set(Meals.MEALS.NAME, meal.getName())
                    .set(Meals.MEALS.PRICE, meal.getPrice())
                    .set(Meals.MEALS.CALORIES, meal.getCalories())
                    .set(Meals.MEALS.ALLERGY, meal.getAllergy())
                    .set(Meals.MEALS.MEAT, meal.getMeat())
                    .execute();

            return dsl.selectFrom(Meals.MEALS)
                    .where(Meals.MEALS.NAME.eq(meal.getName()))
                    .fetchOne();

        }

    }

    /**
     * Method getAllMeals displays all meals in the database
     */
    public List<MealsRecord> getAllMeals() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            return dsl.selectFrom(Meals.MEALS)
                    .fetchInto(MealsRecord.class);
        }
    }

    /**
     * Method getAllAllergies displays all allergies in the database
     */
    public List<MealsRecord> getAllAllergies() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            return dsl.selectFrom(Meals.MEALS)
                    .fetchInto(MealsRecord.class);
        }
    }

    /**
     * Method deleteMeal deletes a meal from the database
     *
     * @param mealId of type int of the meal to be deleted
     */
    public void deleteMeal(int mealId) throws SQLException, MealDoesntExistException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            if (!dsl.fetchExists(
                    dsl.selectFrom(Meals.MEALS)
                            .where(Meals.MEALS.MEAL_ID.eq(mealId)))) {
                throw new MealDoesntExistException("The meal with the given ID doesn't exist!");
            }

            dsl.deleteFrom(Meals.MEALS)
                    .where(Meals.MEALS.MEAL_ID.eq(mealId))
                    .execute();
        }
    }

    /**
     * Method searchMeal searches for a meal by name
     *
     * @param name of type String of the meal to be searched
     */
    public List<MealsRecord> searchMeal(String name) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);

            return dsl.selectFrom(Meals.MEALS)
                    .where(Meals.MEALS.NAME.eq(name))
                    .fetchInto(MealsRecord.class);
        }
    }

    /**
     * Method mealDetails displays the details of a meal
     *
     * @param mealId of type int of the meal to be displayed
     */
    public List<MealsRecord> mealDetails(int mealId) throws SQLException , MealDoesntExistException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            if (!dsl.fetchExists(
                    dsl.selectFrom(Meals.MEALS)
                            .where(Meals.MEALS.MEAL_ID.eq(mealId)))
            ) {
                throw new MealDoesntExistException("Meal with the given iD " + mealId + " doesnt exist !");
            }
            return dsl.selectFrom(Meals.MEALS)
                    .where(Meals.MEALS.MEAL_ID.eq(mealId))
                    .fetchInto(MealsRecord.class);
        }
    }

    /**
     * Method getAllReviews displays all reviews in the database
     */
    public List<ReviewRecord> getAllReviews() throws SQLException{
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            return dsl.selectFrom(Review.REVIEW)
                    .fetchInto(ReviewRecord.class);
        }
    }

    /**
     * Method getAllReviews displays all reviews in the database
     */
    public List<ReviewRecord> getAllReviewsByUser(int userId) throws SQLException{
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            return dsl.selectFrom(Review.REVIEW)
                    .where(Review.REVIEW.USERID.eq(userId))
                    .fetchInto(ReviewRecord.class);
        }
    }

    /**
     * Method reviewByMealiD searches for a review by meal ID
     *
     * @param giveniD of type int of the meal to be searched
     */
    public List<ReviewRecord> reviewsByMealiD(int giveniD) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            return dsl.select()
                    .from(Review.REVIEW)
                    .where(Review.REVIEW.MEAL_ID.eq(giveniD))
                    .fetchInto(ReviewRecord.class);
        }
    }

    /**
     * Method reviewsByMealName searches for reviews by meal name
     *
     * @param mealName of type String of the meal to be searched
     */
    public List<ReviewRecord> reviewsByMealName(String mealName) throws SQLException , MealDoesntExistException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            if (!dsl.fetchExists(
                    dsl.selectFrom(Meals.MEALS)
                            .where(Meals.MEALS.NAME.eq(mealName))
            )) {
                throw new MealDoesntExistException("Meal with name" + mealName + " doesnt exist !");
            }
            return dsl.select()
                    .from(Review.REVIEW)
                    .join(Meals.MEALS)
                    .on(Review.REVIEW.MEAL_ID.eq(Meals.MEALS.MEAL_ID))
                    .where(Meals.MEALS.NAME.eq(mealName))
                    .fetchInto(ReviewRecord.class);
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
            if (!dsl.fetchExists(
                    dsl.selectFrom(Review.REVIEW)
                            .where(Review.REVIEW.RATING_ID.eq(ratingId)))
            ) {
                throw new ReviewiDDoesntExistException("Review iD that was provided  does not exist ! ");
            }
            // deletion of the corresponding review iD
            dsl.deleteFrom(Review.REVIEW)
                    .where(Review.REVIEW.RATING_ID.eq(ratingId))
                    .execute();
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
            dsl.insertInto(Review.REVIEW)
                    .set(Review.REVIEW.MEAL_ID, givenReview.getMealId())
                    .set(Review.REVIEW.RATING, givenReview.getRating())
                    .set(Review.REVIEW.COMMENT, givenReview.getComment())
                    .set(Review.REVIEW.USERID, givenReview.getUserid())
                    .execute();
            return true;
        }
    }

}