package de.htwsaar.cantineplanner.dataAccess;

import de.htwsaar.cantineplanner.businessLogic.AllergenMapper;
import de.htwsaar.cantineplanner.codegen.tables.Review;
import de.htwsaar.cantineplanner.codegen.tables.records.ReviewRecord;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import de.htwsaar.cantineplanner.codegen.tables.records.MealsRecord;
import de.htwsaar.cantineplanner.codegen.tables.Meals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class DBConnection {
    private static final Logger logger = LoggerFactory.getLogger(DBConnection.class);
    private HikariCPDataSource dataSource;

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
        return DSL.using(connection, SQLDialect.SQLITE);
    }

    /**
     * Method addMeal adds a meal for the database
     *
     * @param meal of type MealsRecord to be added
     */
    public void addMeal(MealsRecord meal) {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            dsl.insertInto(Meals.MEALS)
                    .set(Meals.MEALS.NAME, meal.getName())
                    .set(Meals.MEALS.PRICE, meal.getPrice())
                    .set(Meals.MEALS.CALORIES, meal.getCalories())
                    .set(Meals.MEALS.ALLERGY, meal.getAllergy())
                    .execute();
            logger.info("Meal added: {}, Allergy: {}", meal.getName(), meal.getAllergy());
        } catch (SQLException e) {
            logger.error("Error adding meal", e);
        }
    }

    /**
     * Method allMeals displays all meals in the database
     */
    public void allMeals() {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            dsl.selectFrom(Meals.MEALS)
                    .fetch()
                    .forEach(record -> {
                        String allergens = Optional.ofNullable(record.get(Meals.MEALS.ALLERGY))
                                .map(allergyField -> Arrays.stream(allergyField.split(""))
                                        .map(AllergenMapper::getAllergenFullName)
                                        .collect(Collectors.joining(" ")))
                                .orElse("None");
                        System.out.println("Gericht: " + record.get(Meals.MEALS.NAME));
                        System.out.println("Preis: " + record.get(Meals.MEALS.PRICE));
                        System.out.println("Kalorien: " + record.get(Meals.MEALS.CALORIES));
                        System.out.println("Allergene: " + allergens);
                        System.out.println("Fleisch: " + record.get(Meals.MEALS.MEAT));
                        System.out.println("---------");
                    });
            logger.info("All meals displayed");
        } catch (SQLException e) {
            logger.error("Error displaying meals", e);
        }
    }

    /**
     * Method allAllergies displays all allergies in the database
     */
    public void allAllergies() {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            dsl.select(Meals.MEALS.NAME, Meals.MEALS.ALLERGY)
                    .from(Meals.MEALS)
                    .fetch()
                    .forEach(record -> {
                        String allergens = Optional.ofNullable(record.get(Meals.MEALS.ALLERGY))
                                .map(allergyField -> Arrays.stream(allergyField.split(""))
                                        .map(AllergenMapper::getAllergenFullName)
                                        .collect(Collectors.joining(" ")))
                                .orElse("None");
                        System.out.println("Gericht: " + record.get(Meals.MEALS.NAME));
                        System.out.println("Allergie: " + allergens);
                        System.out.println("---------");
                    });
            logger.info("All allergies displayed");
        } catch (SQLException e) {
            logger.error("Error displaying allergies", e);
        }
    }

    /**
     * Method deleteMeal deletes a meal from the database
     *
     * @param mealId of type int of the meal to be deleted
     */
    public void deleteMeal(int mealId) {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            dsl.deleteFrom(Meals.MEALS)
                    .where(Meals.MEALS.MEAL_ID.eq(mealId))
                    .execute();
            System.out.println("Meal with ID " + mealId + " deleted");
        } catch (SQLException e) {
            logger.error("Error deleting meal: ", e);
        }
    }

    /**
     * Method searchMeal searches for a meal by name
     *
     * @param name of type String of the meal to be searched
     */
    public void searchMeal(String name) {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            dsl.selectFrom(Meals.MEALS)
                    .where(Meals.MEALS.NAME.eq(name))
                    .fetch()
                    .forEach(record -> {
                        String allergens = Optional.ofNullable(record.get(Meals.MEALS.ALLERGY))
                                .map(allergyField -> Arrays.stream(allergyField.split(""))
                                        .map(AllergenMapper::getAllergenFullName)
                                        .collect(Collectors.joining(" ")))
                                .orElse("None");
                        System.out.println("Gericht: " + record.get(Meals.MEALS.NAME));
                        System.out.println("Preis: " + record.get(Meals.MEALS.PRICE));
                        System.out.println("Kalorien: " + record.get(Meals.MEALS.CALORIES));
                        System.out.println("Allergene: " + allergens);
                        System.out.println("---------");
                    });
            logger.info("Meal searched: {}", name);
        } catch (SQLException e) {
            logger.error("Error searching for meal: ", e);
        }
    }

    /**
     * Method mealDetails displays the details of a meal
     *
     * @param mealId of type int of the meal to be displayed
     */
    public void mealDetails(int mealId) {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            dsl.selectFrom(Meals.MEALS)
                    .where(Meals.MEALS.MEAL_ID.eq(mealId))
                    .fetch()
                    .forEach(record -> {
                        String allergens = Optional.ofNullable(record.get(Meals.MEALS.ALLERGY))
                                .map(allergyField -> Arrays.stream(allergyField.split(""))
                                        .map(AllergenMapper::getAllergenFullName)
                                        .collect(Collectors.joining(" ")))
                                .orElse("None");
                        System.out.println("Gericht: " + record.get(Meals.MEALS.NAME));
                        System.out.println("Preis: " + record.get(Meals.MEALS.PRICE));
                        System.out.println("Kalorien: " + record.get(Meals.MEALS.CALORIES));
                        System.out.println("Allergene: " + allergens);
                        System.out.println("Fleisch: " + record.get(Meals.MEALS.MEAT));
                        System.out.println("---------");
                    });
            logger.info("Meal details displayed: {}", mealId);
        } catch (SQLException e) {
            logger.error("Error displaying meal details: ", e);
        }
    }

    /**
     * Method allReviews displays all reviews in the database
     */
    public void allReviews() {

        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            dsl.select()  //hi from Herr Bohr
                    .from(Review.REVIEW)
                    .join(Meals.MEALS)
                    .on(Review.REVIEW.MEAL_ID.eq(Meals.MEALS.MEAL_ID))
                    .fetch()
                    .forEach(record -> {
                        System.out.println("Rating iD :" + record.get(Review.REVIEW.RATING_ID));
                        System.out.println("Meal ID : " + record.get(Review.REVIEW.MEAL_ID));
                        System.out.println("Meal Name :" + record.get(Meals.MEALS.NAME));
                        System.out.println("Allergy : " + record.get(Meals.MEALS.ALLERGY));
                        System.out.println("Rating : " + record.get(Review.REVIEW.RATING));
                        System.out.println("Comment : " + record.get(Review.REVIEW.COMMENT));
                        System.out.println("Date created : " + record.get(Review.REVIEW.CREATED_AT));
                        System.out.println("---------");
                    });
            logger.info("All reviews displayed");
        } catch (RuntimeException | SQLException e) {
            logger.error("Error displaying all reviews", e);
        }
    }

    /**
     * Method reviewByMealiD searches for a review by meal ID
     *
     * @param giveniD of type int of the meal to be searched
     */
    public void reviewByMealiD(int giveniD) {

        try (Connection connection = dataSource.getConnection()) {

            DSLContext dsl = getDSLContext(connection);
            dsl.select()
                    .from(Review.REVIEW)
                    .where(Review.REVIEW.MEAL_ID.eq(giveniD))
                    .fetch()
                    .forEach(record -> {
                        System.out.println("Rating iD :" + record.get(Review.REVIEW.RATING_ID));
                        System.out.println("Meal ID : " + record.get(Review.REVIEW.MEAL_ID));
                        System.out.println("Meal Name :" + record.get(Meals.MEALS.NAME));
                        System.out.println("Rating : " + record.get(Review.REVIEW.RATING));
                        System.out.println("Comment : " + record.get(Review.REVIEW.COMMENT));
                        System.out.println("Date created : " + record.get(Review.REVIEW.CREATED_AT));
                        System.out.println("---------");
                    });
            logger.info("Review searched by meal ID: {}", giveniD);
        } catch (RuntimeException | SQLException e) {
            logger.error("Error searching for review by meal ID", e);
        }
    }

    /**
     * Method reviewsByMealName searches for reviews by meal name
     *
     * @param mealName of type String of the meal to be searched
     */
    public void reviewsByMealName(String mealName) {

        try (Connection connection = dataSource.getConnection()) {

            DSLContext dsl = getDSLContext(connection);
            dsl.select()
                    .from(Review.REVIEW)
                    .join(Meals.MEALS)
                    .on(Review.REVIEW.MEAL_ID.eq(Meals.MEALS.MEAL_ID))
                    .where(Meals.MEALS.NAME.eq(mealName))
                    .fetch()
                    .forEach(record -> {
                        System.out.println("Rating iD :" + record.get(Review.REVIEW.RATING_ID));
                        System.out.println("Meal ID : " + record.get(Review.REVIEW.MEAL_ID));
                        System.out.println("Meal Name :" + record.get(Meals.MEALS.NAME));
                        System.out.println("Rating : " + record.get(Review.REVIEW.RATING));
                        System.out.println("Comment : " + record.get(Review.REVIEW.COMMENT));
                        System.out.println("Date created : " + record.get(Review.REVIEW.CREATED_AT));
                        System.out.println("---------");
                    });
            logger.info("Reviews searched by meal name: {}", mealName);
        } catch (RuntimeException | SQLException e) {
            logger.error("Error searching for reviews by meal name", e);
        }
    }

    /**
     * Method deleteReview deletes a review from the database
     *
     * @param ratingId of type int of the review to be deleted
     */
    public void deleteReview(int ratingId) {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            dsl.deleteFrom(Review.REVIEW)
                    .where(Review.REVIEW.RATING_ID.eq(ratingId))
                    .execute();
            System.out.println("Review with ID " + ratingId + " deleted");
            logger.info("Review deleted: {}", ratingId);
        } catch (SQLException e) {
            logger.error("Error deleting review: ", e);
        }
    }

    /**
     * Method addReview adds a review for the database
     *
     * @param givenReview of type ReviewRecord to be added
     */
    public void addReview(ReviewRecord givenReview) {

        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            dsl.insertInto(Review.REVIEW)
                    .set(Review.REVIEW.MEAL_ID, givenReview.getMealId())
                    .set(Review.REVIEW.RATING, givenReview.getRating())
                    .set(Review.REVIEW.COMMENT, givenReview.getComment())
                    .execute();
            System.out.println("Review added for meal with ID " + givenReview.getMealId());
            logger.info("Review added for meal with ID: {}", givenReview.getMealId());
        } catch (SQLException e) {
            logger.error("Error adding review", e);
        }
    }
}