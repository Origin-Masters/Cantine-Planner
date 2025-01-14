package de.htwsaar.cantineplanner.dataAccess;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.htwsaar.cantineplanner.businessLogic.Meal;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;

public class DBConnection {
    private HikariDataSource dataSource;
    private DSLContext dsl;

    public DBConnection() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:src/main/resources/database.db");
        config.setUsername("");
        config.setPassword("");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        this.dataSource = new HikariDataSource(config);
        try (Connection connection = dataSource.getConnection()) {
            this.dsl = DSL.using(connection, SQLDialect.SQLITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createMeal(Meal meal) {
        dsl.insertInto(DSL.table("meals"),
                        DSL.field("Name"), DSL.field("Price"), DSL.field("calories"))
                .values(meal.getName(), meal.getPrice(), meal.getCalories())
                .execute();
        System.out.println("Meal added: " + meal.getName());
    }

    public void allMeals() {
        dsl.selectFrom(DSL.table("meals"))
                .fetch()
                .forEach(record -> {
                    System.out.println("Gericht: " + record.get("Name"));
                    System.out.println("Preis: " + record.get("Price"));
                    System.out.println("Kalorien: " + record.get("calories"));
                    System.out.println("---------");
                });
    }
}