package de.htwsaar.cantineplanner.data;

import de.htwsaar.cantineplanner.codegen.tables.Meals;
import de.htwsaar.cantineplanner.codegen.tables.records.MealsRecord;
import de.htwsaar.cantineplanner.dataAccess.DBConnection;
import de.htwsaar.cantineplanner.dataAccess.HikariCPDataSource;
import de.htwsaar.cantineplanner.exceptions.MealAlreadyExistsException;
import de.htwsaar.cantineplanner.exceptions.MealDoesntExistException;
import de.htwsaar.cantineplanner.exceptions.MealiDNotFoundException;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.UpdateSetFirstStep;
import org.jooq.UpdateSetMoreStep;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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






}