package de.htwsaar.cantineplanner.dataAccess;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import de.htwsaar.cantineplanner.codegen.tables.records.MealsRecord;
import de.htwsaar.cantineplanner.codegen.tables.Meals;

import java.sql.Connection;
import java.net.URL;
import java.nio.file.Paths;

public class DBConnection {
    private static HikariDataSource dataSource;
    private static DSLContext dsl;

    static {
        try {
            URL resource = DBConnection.class.getClassLoader().getResource("database.db");
            if (resource == null) {
                throw new IllegalStateException("Database file not found!");
            }
            String dbPath = Paths.get(resource.toURI()).toString();

            HikariConfig config = new HikariConfig("/hikari.properties");
            config.setJdbcUrl("jdbc:sqlite:" + dbPath);
            dataSource = new HikariDataSource(config);

            Connection connection = dataSource.getConnection();
            dsl = DSL.using(connection, SQLDialect.SQLITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addMeal(MealsRecord meal) {
        dsl.insertInto(Meals.MEALS)
                .set(Meals.MEALS.NAME, meal.getName())
                .set(Meals.MEALS.PRICE, meal.getPrice())
                .set(Meals.MEALS.CALORIES, meal.getCalories())
                .execute();
        System.out.println("Meal added: " + meal.getName());
    }

    public void allMeals() {
        dsl.selectFrom(Meals.MEALS)
                .fetch()
                .forEach(record -> {
                    System.out.println("Gericht: " + record.get(Meals.MEALS.NAME));
                    System.out.println("Preis: " + record.get(Meals.MEALS.PRICE));
                    System.out.println("Kalorien: " + record.get(Meals.MEALS.CALORIES));
                    System.out.println("---------");
                });
    }

    public void allAllergies() {
        dsl.select(Meals.MEALS.ALLERGY)
                .from(Meals.MEALS)
                .fetch()
                .forEach(record -> {
                    System.out.println("Allergie: " + record.get(Meals.MEALS.ALLERGY));
                });
    }
}