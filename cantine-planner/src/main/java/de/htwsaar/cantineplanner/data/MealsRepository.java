package de.htwsaar.cantineplanner.data;

import de.htwsaar.cantineplanner.codegen.tables.Meals;
import de.htwsaar.cantineplanner.codegen.tables.Review;
import de.htwsaar.cantineplanner.codegen.tables.Users;
import de.htwsaar.cantineplanner.codegen.tables.records.MealsRecord;
import de.htwsaar.cantineplanner.dataAccess.HikariCPDataSource;
import de.htwsaar.cantineplanner.exceptions.MealAlreadyExistsException;
import de.htwsaar.cantineplanner.exceptions.MealDoesntExistException;
import de.htwsaar.cantineplanner.exceptions.MealiDNotFoundException;
import de.htwsaar.cantineplanner.exceptions.UserDoesntExistException;
import org.jooq.UpdateSetFirstStep;
import org.jooq.UpdateSetMoreStep;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The MealsRepository class is responsible for handling meal data in the database.
 */
public class MealsRepository extends AbstractRepository {
    /**
     * Constructor for MealsRepository
     *
     * @param dataSource an instance of HikariCPDataSource, offering a connection pool
     * for efficient and reliable database connectivity.
     */
    protected MealsRepository(HikariCPDataSource dataSource) {
        super(dataSource);
    }

    /**
     * Updates the specified meal record in the database.
     * <p>
     * This method dynamically updates the meal fields based on the non-null
     * values provided in the given meal record. It first verifies that a meal
     * with the specified meal ID exists in the database. If the meal does not
     * exist, a MealDoesntExistException is thrown. Otherwise, it constructs
     * the update query using jOOQ and executes it.
     * </p>
     *
     * @param meal a MealsRecord instance containing the updated meal data;
     *             non-null fields will be updated in the database
     * @throws SQLException             if a database error occurs during connection or query execution
     * @throws MealDoesntExistException if the meal with the specified ID is not found
     */
    protected void editMeal(MealsRecord meal) throws SQLException, MealDoesntExistException {
        try (Connection connection = dataSource.getConnection()) {
            var dsl = super.getDSLContext(connection);

            if (!dsl.fetchExists(dsl.selectFrom(Meals.MEALS).where(Meals.MEALS.MEAL_ID.eq(meal.getMealId())))) {
                throw new MealDoesntExistException("The meal with the given ID doesn't exist!");
            }

            UpdateSetFirstStep<MealsRecord> updateQuery = dsl.update(Meals.MEALS);
            UpdateSetMoreStep<MealsRecord> setSteps = null;

            if (meal.getName() != null) {
                setSteps = updateQuery.set(Meals.MEALS.NAME, meal.getName());
            }
            if (meal.getPrice() != null) {
                setSteps = (setSteps == null)
                        ? updateQuery.set(Meals.MEALS.PRICE, meal.getPrice())
                        : setSteps.set(Meals.MEALS.PRICE, meal.getPrice());
            }
            if (meal.getCalories() != null) {
                setSteps = (setSteps == null)
                        ? updateQuery.set(Meals.MEALS.CALORIES, meal.getCalories())
                        : setSteps.set(Meals.MEALS.CALORIES, meal.getCalories());
            }
            if (meal.getAllergy() != null) {
                setSteps = (setSteps == null)
                        ? updateQuery.set(Meals.MEALS.ALLERGY, meal.getAllergy())
                        : setSteps.set(Meals.MEALS.ALLERGY, meal.getAllergy());
            }
            if (meal.getMeat() != null) {
                setSteps = (setSteps == null)
                        ? updateQuery.set(Meals.MEALS.MEAT, meal.getMeat())
                        : setSteps.set(Meals.MEALS.MEAT, meal.getMeat());
            }
            if (setSteps != null) {
                setSteps.where(Meals.MEALS.MEAL_ID.eq(meal.getMealId()))
                        .execute();
            }
        }
    }

    /**
     * Adds a new meal record to the database.
     * <p>
     * This method first checks if a meal with the same name already exists in the database.
     * If the meal already exists, a MealAlreadyExistsException is thrown. Otherwise, it
     * inserts the new meal record into the Meals table and returns the inserted record.
     * </p>
     *
     * @param meal a MealsRecord instance containing the meal data to be added
     * @return the inserted MealsRecord object
     * @throws SQLException               if a database access error occurs
     * @throws MealAlreadyExistsException if a meal with the same name already exists
     */
    protected Optional<MealsRecord> addMeal(MealsRecord meal) throws SQLException, MealAlreadyExistsException {
        try (var connection = dataSource.getConnection()) {
            var dsl = getDSLContext(connection);
            if (dsl.fetchExists(dsl.selectFrom(Meals.MEALS).where(Meals.MEALS.NAME.eq(meal.getName())))) {
                throw new MealAlreadyExistsException("Meal already exists!");
            }

            dsl.insertInto(Meals.MEALS)
                    .set(Meals.MEALS.NAME, meal.getName())
                    .set(Meals.MEALS.PRICE, meal.getPrice())
                    .set(Meals.MEALS.CALORIES, meal.getCalories())
                    .set(Meals.MEALS.ALLERGY, meal.getAllergy())
                    .set(Meals.MEALS.MEAT, meal.getMeat())
                    .set(Meals.MEALS.DAY, meal.getDay())
                    .execute();

            // Rückgabe als Optional, um den Fall eines fehlenden Datensatzes explizit zu behandeln:
            return Optional.ofNullable(dsl.selectFrom(Meals.MEALS)
                    .where(Meals.MEALS.NAME.eq(meal.getName()))
                    .fetchOne());
        }
    }


    /**
     * Retrieves all meal records from the database.
     * <p>
     * This method fetches all records from the Meals table. The results are returned as
     * a list of MealsRecord objects.
     * </p>
     *
     * @return a list of MealsRecord objects representing all meals in the database
     * @throws SQLException if a database access error occurs
     */
    protected List<MealsRecord> getAllMeals() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            var dsl = getDSLContext(connection);
            return dsl.selectFrom(Meals.MEALS).fetchInto(MealsRecord.class);
        }
    }

    /**
     * Retrieves all meal records with allergy information from the database.
     * <p>
     * This method fetches all records from the Meals table. The results are returned as
     * a list of MealsRecord objects.
     * </p>
     *
     * @return a list of MealsRecord objects representing all meals with allergy information in the database
     * @throws SQLException if a database access error occurs
     */
    protected List<MealsRecord> getAllAllergies() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            var dsl = getDSLContext(connection);
            return dsl.selectFrom(Meals.MEALS).fetchInto(MealsRecord.class);
        }
    }

    /**
     * Deletes a meal record from the database by meal ID.
     * <p>
     * This method first checks if a meal with the specified ID exists in the database.
     * If the meal does not exist, a MealiDNotFoundException is thrown. Otherwise, it
     * deletes the meal record from the Meals table.
     * </p>
     *
     * @param mealId the ID of the meal to be deleted
     * @throws SQLException            if a database access error occurs
     * @throws MealiDNotFoundException if the meal with the specified ID is not found
     */
    protected void deleteMealById(int mealId) throws SQLException, MealiDNotFoundException {
        try (Connection connection = dataSource.getConnection()) {
            var dsl = getDSLContext(connection);
            if (!dsl.fetchExists(dsl.selectFrom(Meals.MEALS).where(Meals.MEALS.MEAL_ID.eq(mealId)))) {
                throw new MealiDNotFoundException("The meal with the given ID doesn't exist!");
            }

            dsl.deleteFrom(Meals.MEALS).where(Meals.MEALS.MEAL_ID.eq(mealId)).execute();
        }
    }

    /**
     * Searches for a meal by its name in the database.
     * <p>
     * This method checks if a meal with the specified name exists in the database.
     * If the meal does not exist, a MealDoesntExistException is thrown. Otherwise,
     * it fetches and returns the meal records that match the given name.
     * </p>
     *
     * @param name the name of the meal to be searched
     * @return a list of MealsRecord objects representing the meals with the specified name
     * @throws SQLException             if a database access error occurs
     * @throws MealDoesntExistException if a meal with the specified name is not found
     */
    protected List<MealsRecord> searchMealByName(String name) throws SQLException, MealDoesntExistException {
        try (Connection connection = dataSource.getConnection()) {
            var dsl = getDSLContext(connection);
            if (!dsl.fetchExists(dsl.selectFrom(Meals.MEALS).where(Meals.MEALS.NAME.eq(name)))) {
                throw new MealDoesntExistException("Meal with name " + name + " doesn't exist!");
            }

            return dsl.selectFrom(Meals.MEALS).where(Meals.MEALS.NAME.eq(name)).fetchInto(MealsRecord.class);
        }
    }

    /**
     * Searches for a meal by its ID in the database.
     * <p>
     * This method checks if a meal with the specified ID exists in the database.
     * If the meal does not exist, a MealiDNotFoundException is thrown. Otherwise,
     * it fetches and returns the meal records that match the given ID.
     * </p>
     *
     * @param mealId the ID of the meal to be searched
     * @return a list of MealsRecord objects representing the meals with the specified ID
     * @throws SQLException            if a database access error occurs
     * @throws MealiDNotFoundException if a meal with the specified ID is not found
     */
    protected List<MealsRecord> searchMealById(int mealId) throws SQLException, MealiDNotFoundException {
        try (Connection connection = dataSource.getConnection()) {
            var dsl = getDSLContext(connection);
            if (!dsl.fetchExists(dsl.selectFrom(Meals.MEALS).where(Meals.MEALS.MEAL_ID.eq(mealId)))) {
                throw new MealiDNotFoundException("Meal with the given ID " + mealId + " doesn't exist!");
            }
            return dsl.selectFrom(Meals.MEALS).where(Meals.MEALS.MEAL_ID.eq(mealId)).fetchInto(MealsRecord.class);
        }
    }

    /**
     * Sorts meals by price in ascending order.
     * <p>
     * This method fetches all meal records from the database and sorts them by price in ascending order.
     * </p>
     *
     * @return a list of MealsRecord objects sorted by price
     * @throws SQLException if a database access error occurs
     */
    protected List<MealsRecord> sortMealsByPrice() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            var dsl = getDSLContext(connection);
            return dsl.selectFrom(Meals.MEALS)
                    .orderBy(Meals.MEALS.PRICE.asc())
                    .fetchInto(MealsRecord.class);
        }
    }

    /**
     * Sorts meals by their median rating in descending order.
     * <p>
     * This method fetches all meal records from the database, calculates the median rating for each meal,
     * and sorts the meals by their median rating in descending order.
     * </p>
     *
     * @return a list of MealsRecord objects sorted by their median rating
     * @throws SQLException if a database access error occurs
     */
    protected List<MealsRecord> sortMealsByRating() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            var dsl = getDSLContext(connection);

            // Fetch all meals
            List<MealsRecord> meals = dsl.selectFrom(Meals.MEALS).fetchInto(MealsRecord.class);

            // Calculate median rating for each meal
            Map<Integer, Double> mealRatings = new HashMap<>();
            for (MealsRecord record : meals) {
                Double medianRating = dsl.select(DSL.median(Review.REVIEW.RATING))
                        .from(Review.REVIEW)
                        .where(Review.REVIEW.MEAL_ID.eq(record.getMealId()))
                        .fetchOne(0, double.class);
                mealRatings.put(record.getMealId(), medianRating != null ? medianRating : 0.0);
            }

            // Sort meals by calculated median rating
            meals.sort((m1, m2) -> Double.compare(mealRatings.get(m2.getMealId()), mealRatings.get(m1.getMealId())));
            return meals;
        }
    }

    /**
     * Sorts meals by name in ascending order.
     * <p>
     * This method fetches all meal records from the database and sorts them by name in ascending order.
     * </p>
     *
     * @return a list of MealsRecord objects sorted by name
     * @throws SQLException if a database access error occurs
     */
    protected List<MealsRecord> sortMealsByName() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            var dsl = getDSLContext(connection);
            return dsl.selectFrom(Meals.MEALS)
                    .orderBy(Meals.MEALS.NAME.asc())
                    .fetchInto(MealsRecord.class);
        }
    }

    /**
     * Sorts meals by calories in ascending order.
     * <p>
     * This method fetches all meal records from the database and sorts them by calories in ascending order.
     * </p>
     *
     * @return a list of MealsRecord objects sorted by calories
     * @throws SQLException if a database access error occurs
     */
    protected List<MealsRecord> sortMealsByCalories() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            var dsl = getDSLContext(connection);
            return dsl.selectFrom(Meals.MEALS)
                    .orderBy(Meals.MEALS.CALORIES.asc())
                    .fetchInto(MealsRecord.class);
        }
    }

    /**
     * Sorts meals by excluding those that contain the user's allergies.
     * <p>
     * This method retrieves all meal records from the database and filters out
     * meals that contain any of the user's allergies. The user's allergies are
     * fetched based on the provided user ID.
     * </p>
     *
     * @param userId the ID of the user
     * @return a list of MealsRecord objects excluding those with the user's allergies
     * @throws SQLException             if a database access error occurs
     * @throws UserDoesntExistException if the user with the given ID doesn't exist
     */
    protected List<MealsRecord> sortMealsByAllergy(int userId) throws SQLException, UserDoesntExistException {
        List<String> userAllergies = getUserAllergies(userId);

        try (Connection connection = dataSource.getConnection()) {
            var dsl = getDSLContext(connection);

            // Fetch all meals
            List<MealsRecord> meals = dsl.selectFrom(Meals.MEALS).fetchInto(MealsRecord.class);

            // Filter out meals that contain user's allergies
            return meals.stream()
                    .filter(meal -> {
                        String[] mealAllergies = meal.getAllergy().split(",");
                        for (String allergy : mealAllergies) {
                            if (userAllergies.contains(allergy.trim())) {
                                return false;
                            }
                        }
                        return true;
                    })
                    .collect(Collectors.toList());
        }
    }

    /**
     * Retrieves the allergy settings for a user by user ID.
     * <p>
     * This method fetches the user's allergy information from the database based on the provided user ID.
     * </p>
     *
     * @param userId the ID of the user
     * @return a list of allergies
     * @throws SQLException             if a database access error occurs
     * @throws UserDoesntExistException if the user with the given ID doesn't exist
     */
    private List<String> getUserAllergies(int userId) throws SQLException, UserDoesntExistException {
        try (Connection connection = dataSource.getConnection()) {
            var dsl = getDSLContext(connection);

            var user = dsl.selectFrom(Users.USERS)
                    .where(Users.USERS.USERID.eq(userId))
                    .fetchOne();

            if (user == null) {
                throw new UserDoesntExistException("The user with the given UserId doesn't exist!");
            }

            String allergies = user.getDontShowMeal();
            return Arrays.asList(allergies.split(","));
        }
    }

    /**
     * Calculates the median rating for a meal.
     * <p>
     * This method calculates the median rating for a meal based on the provided meal ID.
     * </p>
     *
     * @param mealId the ID of the meal to calculate the median rating for
     * @return the median rating of the meal
     * @throws SQLException if a database access error occurs
     */
    //FIXME
    protected double calculateMedianRatingForMeal(int mealId) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            var dsl = getDSLContext(connection);
            return dsl.select(DSL.median(Review.REVIEW.RATING))
                    .from(Review.REVIEW)
                    .where(Review.REVIEW.MEAL_ID.eq(mealId))
                    .fetchOne(0, double.class);
        }
    }
}