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
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DBConnection {
    private static final Logger logger = LoggerFactory.getLogger(DBConnection.class);
    private final HikariCPDataSource dataSource;
    private final EventManager eventManager;

    /**
     * Constructor for DBConnection creates a new HikariCPDataSource
     */
    public DBConnection(EventManager eventManager) {
        this.dataSource = new HikariCPDataSource();
        this.eventManager = eventManager;
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
     * Method validateUser validates a user
     *
     * @param username          of type String
     * @param plainTextPassword of type String
     * @throws UserNotValidatedException if the user is not validated
     */

    public boolean validateUser(String username, String plainTextPassword) {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            String hashedPassword = dsl.select(Users.USERS.PASSWORD)
                    .from(Users.USERS)
                    .where(Users.USERS.USERNAME.eq(username))
                    .fetchOne(Users.USERS.PASSWORD);

            if (hashedPassword == null || !PasswordUtil.verifyPassword(plainTextPassword, hashedPassword)) {
                eventManager.notify("error", "Invalid username or password !");
            }
            return true;
        } catch (SQLException e) {
            eventManager.notify("error", "Something went wrong during the validation process ");
        }
        return false;
    }

    public int getUserId(String username) {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);

            if (!dsl.fetchExists(
                    dsl.selectFrom(Users.USERS)
                            .where(Users.USERS.USERNAME.eq(username)))) {
                throw new UserDoesntExistException("The user with the given username doesnt exist !");
            }


            return dsl.select(Users.USERS.USERID)
                    .from(Users.USERS)
                    .where(Users.USERS.USERNAME.eq(username))
                    .fetchOne(Users.USERS.USERID);
        } catch (NullPointerException | SQLException e) {

            eventManager.notify("error", "Error retrieving  user ID !");

            return -1;
        }
    }

    public UsersRecord registerUser(String username, String plainTextPassword, String email) {

        UsersRecord user = new UsersRecord();

        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);

            // Check if username already exists
            if (
                    dsl.fetchExists(
                            dsl.selectFrom(Users.USERS)
                                    .where(Users.USERS.USERNAME.eq(username)))) {
                throw new UserAlreadyExistsException("Username already exists please choose another one!"
                );
            }

            String hashedPassword = PasswordUtil.hashPassword(plainTextPassword);
            dsl.insertInto(Users.USERS)
                    .set(Users.USERS.USERNAME, username)
                    .set(Users.USERS.PASSWORD, hashedPassword)
                    .set(Users.USERS.EMAIL, email)
                    .set(Users.USERS.ROLE, 0)
                    .execute();

            // retrieve user record
            user = dsl.selectFrom(Users.USERS)
                    .where(Users.USERS.USERNAME.eq(username))
                    .fetchOne();

        } catch (SQLException e) {
            eventManager.notify("error", "Error registering user");
        } catch (UserAlreadyExistsException e) {
            eventManager.notify("error", "Username already exists");
        }

        return user;
    }

    /**
     * Method addMeal adds a meal for the database
     *
     * @param meal of type MealsRecord to be added
     */
    public MealsRecord addMeal(MealsRecord meal) {

        MealsRecord mealOutput = new MealsRecord();
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);

            if (
                    dsl.fetchExists(
                            dsl.selectFrom(Meals.MEALS)
                                    .where(Meals.MEALS.NAME.eq(meal.getName()))
                    )
            ) {
                throw new MealAlreadyExistsException(" Meal Already exists !");
            }

            dsl.insertInto(Meals.MEALS)
                    .set(Meals.MEALS.NAME, meal.getName())
                    .set(Meals.MEALS.PRICE, meal.getPrice())
                    .set(Meals.MEALS.CALORIES, meal.getCalories())
                    .set(Meals.MEALS.ALLERGY, meal.getAllergy())
                    .execute();


            mealOutput = dsl.selectFrom(Meals.MEALS)
                    .where(Meals.MEALS.NAME.eq(meal.getName()))
                    .fetchOne();

        } catch (SQLException e) {
            eventManager.notify("error", "Error adding meal ! ");
        } catch (MealAlreadyExistsException e) {
            eventManager.notify("error", "Meal already exists ! ");
        }

        return mealOutput;
    }

    /**
     * Method getAllMeals displays all meals in the database
     */
    public List<MealsRecord> getAllMeals() {
        List<MealsRecord> mealsList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            dsl.selectFrom(Meals.MEALS)
                    .fetch()
                    .forEach(record -> mealsList.add(record.into(MealsRecord.class)));

        } catch (SQLException e) {
            eventManager.notify("error", "Error getting all meals");
        }
        return mealsList;
    }

    /**
     * Method getAllAllergies displays all allergies in the database
     */
    public List<MealsRecord> getAllAllergies() {
        List<MealsRecord> allergiesList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            dsl.selectFrom(Meals.MEALS)
                    .fetch()
                    .forEach(record -> allergiesList.add(record.into(MealsRecord.class)));

        } catch (SQLException e) {
            eventManager.notify("error", "Error getting all Allergies");
        }
        return allergiesList;
    }

    /**
     * Method deleteMeal deletes a meal from the database
     *
     * @param mealId of type int of the meal to be deleted
     */
    public void deleteMeal(int mealId) {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);

            if (!dsl.fetchExists(
                    dsl.selectFrom(Meals.MEALS)
                            .where(Meals.MEALS.MEAL_ID.eq(mealId)))
            ) {
                throw new MealDoesntExistException(" The Meal with the given iD doesnt exist !");
            }


            dsl.deleteFrom(Meals.MEALS)
                    .where(Meals.MEALS.MEAL_ID.eq(mealId))
                    .execute();



        } catch (SQLException e) {
            eventManager.notify("error", "Error deleting meal !");
        } catch (MealDoesntExistException e) {
            eventManager.notify("error", "Meal with the given iD doesnt exist !");
        }
    }

    /**
     * Method searchMeal searches for a meal by name
     *
     * @param name of type String of the meal to be searched
     */
    public List<MealsRecord> searchMeal(String name) {
        List<MealsRecord> mealsList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);

            dsl.selectFrom(Meals.MEALS)
                    .where(Meals.MEALS.NAME.eq(name))
                    .fetchInto(MealsRecord.class);
        } catch (SQLException e) {
            eventManager.notify("error", "Error searching meal");
        }
        return mealsList;
    }

    /**
     * Method mealDetails displays the details of a meal
     *
     * @param mealId of type int of the meal to be displayed
     */
    public List<MealsRecord> mealDetails(int mealId) {
        List<MealsRecord> mealsList = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {

            DSLContext dsl = getDSLContext(connection);

            if (!dsl.fetchExists(
                    dsl.selectFrom(Meals.MEALS)
                            .where(Meals.MEALS.MEAL_ID.eq(mealId)))
            ) {
                throw new MealDoesntExistException("Meal with the given iD " + mealId + " doesnt exist !");
            }

            mealsList = dsl.selectFrom(Meals.MEALS)
                    .where(Meals.MEALS.MEAL_ID.eq(mealId))
                    .fetchInto(MealsRecord.class);


        } catch (SQLException e) {
            eventManager.notify("error", "Error getting meal details");
        } catch (MealDoesntExistException e) {
            eventManager.notify("error", "Meal with the given iD doesnt exist !");
        }

        return mealsList;
    }

    /**
     * Method getAllReviews displays all reviews in the database
     */
    public List<ReviewRecord> getAllReviews() {
        List<ReviewRecord> reviewsList = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {

            DSLContext dsl = getDSLContext(connection);
            reviewsList = dsl.selectFrom(Review.REVIEW)
                    .fetchInto(ReviewRecord.class);


        } catch (SQLException e) {
            eventManager.notify("error", "Error getting all reviews");
        }
        return reviewsList;
    }

    /**
     * Method reviewByMealiD searches for a review by meal ID
     *
     * @param giveniD of type int of the meal to be searched
     */
    public List<ReviewRecord> reviewByMealiD(int giveniD) {
        List<ReviewRecord> reviewsList = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {

            DSLContext dsl = getDSLContext(connection);
            reviewsList = dsl.select()
                    .from(Review.REVIEW)
                    .where(Review.REVIEW.MEAL_ID.eq(giveniD))
                    .fetchInto(ReviewRecord.class);

        } catch (RuntimeException | SQLException e) {
            eventManager.notify("error", "Error getting review by meal ID");
        }
        return reviewsList;
    }

    /**
     * Method reviewsByMealName searches for reviews by meal name
     *
     * @param mealName of type String of the meal to be searched
     */
    public List<ReviewRecord> reviewsByMealName(String mealName) throws SQLException {
        List<ReviewRecord> reviewsList = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {

            DSLContext dsl = getDSLContext(connection);

            if (!dsl.fetchExists(
                    dsl.selectFrom(Meals.MEALS)
                            .where(Meals.MEALS.NAME.eq(mealName))
            )) {
                throw new MealDoesntExistException("Meal with name" + mealName + " doesnt exist !");
            }


            reviewsList = dsl.select()
                    .from(Review.REVIEW)
                    .join(Meals.MEALS)
                    .on(Review.REVIEW.MEAL_ID.eq(Meals.MEALS.MEAL_ID))
                    .where(Meals.MEALS.NAME.eq(mealName))
                    .fetchInto(ReviewRecord.class);
        } catch (MealDoesntExistException e) {
            eventManager.notify("error", "Meal with the given name doesnt exist !");
        } catch (RuntimeException | SQLException e) {
            eventManager.notify("error", "Error getting reviews by meal name");
        }

        return reviewsList;
    }


    /**
     * Method deleteReview deletes a review from the database
     *
     * @param ratingId of type int of the review to be deleted
     */
    public void deleteReview(int ratingId) {


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

        } catch (ReviewiDDoesntExistException e) {
            eventManager.notify("error", "Review iD provided  does not exist ! ");
        } catch (SQLException e) {
            eventManager.notify("error", "Error deleting review");
        }

    }

    /**
     * Method addReview adds a review for the database
     *
     * @param givenReview of type ReviewRecord to be added
     */
    public boolean addReview(ReviewRecord givenReview, EventManager eventManager) {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            dsl.insertInto(Review.REVIEW)
                    .set(Review.REVIEW.MEAL_ID, givenReview.getMealId())
                    .set(Review.REVIEW.RATING, givenReview.getRating())
                    .set(Review.REVIEW.COMMENT, givenReview.getComment())
                    .execute();
            return true;
        } catch (SQLException e) {
            eventManager.notify("error", "Error adding review: " + e.getMessage());
            return false;
        }
    }

}