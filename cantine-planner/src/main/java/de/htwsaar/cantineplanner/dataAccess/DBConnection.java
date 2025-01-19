package de.htwsaar.cantineplanner.dataAccess;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import de.htwsaar.cantineplanner.codegen.tables.records.MealsRecord;
import de.htwsaar.cantineplanner.codegen.tables.Meals;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {

    private DSLContext getDSLContext(Connection connection) {
        return DSL.using(connection, SQLDialect.SQLITE);
    }

    public void addMeal(MealsRecord meal) {
        try (Connection connection = HikariCPDataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            dsl.insertInto(Meals.MEALS)
                    .set(Meals.MEALS.NAME, meal.getName())
                    .set(Meals.MEALS.PRICE, meal.getPrice())
                    .set(Meals.MEALS.CALORIES, meal.getCalories())
                    .set(Meals.MEALS.ALLERGY, meal.getAllergy())
                    .execute();
            System.out.println("Meal added: " + meal.getName() + ", Allergy: " + meal.getAllergy());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void allMeals() {
        try (Connection connection = HikariCPDataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            dsl.selectFrom(Meals.MEALS)
                    .fetch()
                    .forEach(record -> {
                        System.out.println("Gericht: " + record.get(Meals.MEALS.NAME));
                        System.out.println("Preis: " + record.get(Meals.MEALS.PRICE));
                        System.out.println("Kalorien: " + record.get(Meals.MEALS.CALORIES));
                        System.out.println("---------");
                    });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void allAllergies() {
        try (Connection connection = HikariCPDataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            dsl.select(Meals.MEALS.NAME, Meals.MEALS.ALLERGY)
                    .from(Meals.MEALS)
                    .fetch()
                    .forEach(record -> {
                        System.out.println("Gericht: " + record.get(Meals.MEALS.NAME));
                        System.out.println("Allergie: " + record.get(Meals.MEALS.ALLERGY));
                        System.out.println("---------");
                    });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMeal(int mealId) {
        try (Connection connection = HikariCPDataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            dsl.deleteFrom(Meals.MEALS)
                    .where(Meals.MEALS.MEAL_ID.eq(mealId))
                    .execute();
            System.out.println("Meal with ID " + mealId + " deleted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void searchMeal(String name) {
        try (Connection connection = HikariCPDataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            dsl.selectFrom(Meals.MEALS)
                    .where(Meals.MEALS.NAME.eq(name))
                    .fetch()
                    .forEach(record -> {
                        System.out.println("Gericht: " + record.get(Meals.MEALS.NAME));
                        System.out.println("Preis: " + record.get(Meals.MEALS.PRICE));
                        System.out.println("Kalorien: " + record.get(Meals.MEALS.CALORIES));
                        System.out.println("---------");
                    });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void mealDetails(int mealId) {
        try (Connection connection = HikariCPDataSource.getConnection()) {
            DSLContext dsl = getDSLContext(connection);
            dsl.selectFrom(Meals.MEALS)
                    .where(Meals.MEALS.MEAL_ID.eq(mealId))
                    .fetch()
                    .forEach(record -> {
                        System.out.println("Gericht: " + record.get(Meals.MEALS.NAME));
                        System.out.println("Preis: " + record.get(Meals.MEALS.PRICE));
                        System.out.println("Kalorien: " + record.get(Meals.MEALS.CALORIES));
                        System.out.println("Allergie: " + record.get(Meals.MEALS.ALLERGY));
                        System.out.println("Fleisch: " + record.get(Meals.MEALS.MEAT));
                        System.out.println("---------");
                    });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}