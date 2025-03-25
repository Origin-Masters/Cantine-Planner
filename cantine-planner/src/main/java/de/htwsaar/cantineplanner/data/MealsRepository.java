package de.htwsaar.cantineplanner.data;

import de.htwsaar.cantineplanner.codegen.tables.Meals;
import de.htwsaar.cantineplanner.codegen.tables.Review;
import de.htwsaar.cantineplanner.codegen.tables.Users;
import de.htwsaar.cantineplanner.codegen.tables.records.MealsRecord;
import de.htwsaar.cantineplanner.codegen.tables.records.UsersRecord;
import de.htwsaar.cantineplanner.dataAccess.HikariCPDataSource;
import de.htwsaar.cantineplanner.exceptions.MealAlreadyExistsException;
import de.htwsaar.cantineplanner.exceptions.MealDoesntExistException;
import de.htwsaar.cantineplanner.exceptions.MealiDNotFoundException;
import de.htwsaar.cantineplanner.exceptions.UserDoesntExistException;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.UpdateSetFirstStep;
import org.jooq.UpdateSetMoreStep;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MealsRepository extends AbstractRepository{

    public MealsRepository(HikariCPDataSource dataSource) {
        super(dataSource);
    }

    public void editMeal(MealsRecord meal) throws SQLException, MealDoesntExistException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = super.getDSLContext(connection);

            if (!dsl.fetchExists(dsl.selectFrom(Meals.MEALS).where(Meals.MEALS.MEAL_ID.eq(meal.getMealId())))) {
                throw new MealDoesntExistException("The meal with the given ID doesn't exist!");
            }

            UpdateSetFirstStep<MealsRecord> updateQuery = dsl.update(Meals.MEALS);
            UpdateSetMoreStep<MealsRecord> setSteps = null;

            if (meal.getName() != null) {
                setSteps = (setSteps == null)
                        ? updateQuery.set(Meals.MEALS.NAME, meal.getName())
                        : setSteps.set(Meals.MEALS.NAME, meal.getName());
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


    public List<MealsRecord> sortMealsByPrice() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            return dsl.selectFrom(Meals.MEALS)
                    .orderBy(Meals.MEALS.PRICE.asc())
                    .fetchInto(MealsRecord.class);
        }
    }

    public List<MealsRecord> sortMealsByRating() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);

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

    public List<MealsRecord> sortMealsByName() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            return dsl.selectFrom(Meals.MEALS)
                    .orderBy(Meals.MEALS.NAME.asc())
                    .fetchInto(MealsRecord.class);
        }
    }

    public List<MealsRecord> sortMealsByCalories() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            return dsl.selectFrom(Meals.MEALS)
                    .orderBy(Meals.MEALS.CALORIES.asc())
                    .fetchInto(MealsRecord.class);
        }
    }

    /**
     * Sorts meals by excluding those that contain the user's allergies.
     *
     * @param userId the ID of the user
     * @return a list of meals excluding those with the user's allergies
     * @throws SQLException             if a database access error occurs
     * @throws UserDoesntExistException if the user with the given ID doesn't exist
     */
    public List<MealsRecord> sortMealsByAllergy(int userId) throws SQLException, UserDoesntExistException {
        List<String> userAllergies = getUserAllergies(userId);

        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);

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
     *
     * @param userId the ID of the user
     * @return a list of allergies
     * @throws SQLException             if a database access error occurs
     * @throws UserDoesntExistException if the user with the given ID doesn't exist
     */
    private List<String> getUserAllergies(int userId) throws SQLException, UserDoesntExistException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);

            UsersRecord user = dsl.selectFrom(Users.USERS)
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
     * Method calculateMedianRatingForMeal calculates the median rating for a meal
     *
     * @param mealId of type int of the meal to be calculated
     */
    public double calculateMedianRatingForMeal(int mealId) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            return dsl.select(DSL.median(Review.REVIEW.RATING)).from(Review.REVIEW).where(
                    Review.REVIEW.MEAL_ID.eq(mealId)).fetchOne(0, double.class);
        }
    }



}