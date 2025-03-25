package de.htwsaar.cantineplanner.data;

import de.htwsaar.cantineplanner.codegen.tables.Meals;
import de.htwsaar.cantineplanner.codegen.tables.records.MealsRecord;
import de.htwsaar.cantineplanner.dataAccess.DBConnection;
import de.htwsaar.cantineplanner.dataAccess.HikariCPDataSource;
import de.htwsaar.cantineplanner.exceptions.MealDoesntExistException;
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

    private DSLContext getDSLContext(Connection connection) {
        return DSL.using(connection, SQLDialect.SQLITE);
    }

    public void editMeal(MealsRecord meal) throws SQLException, MealDoesntExistException {
        try (Connection connection = dataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);

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
}