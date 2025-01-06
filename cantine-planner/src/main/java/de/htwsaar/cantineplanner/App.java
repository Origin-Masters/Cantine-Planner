package de.htwsaar.cantineplanner;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class App {
    public static void main(String[] args) {
        String dbUrl = "jdbc:sqlite:src/main/resources/database.db"; // Pfad zur SQLite-Datenbank

        try (Connection connection = DriverManager.getConnection(dbUrl)) {
            DSLContext create = DSL.using(connection, SQLDialect.SQLITE);

            // SQL-Abfrage
            String sql = """
                SELECT dishes.name, ratings.rating, ratings.comment
                FROM ratings
                JOIN dishes ON ratings.dish_id = dishes.dish_id
                WHERE dishes.featured = 1;
            """;

            ResultSet result = connection.createStatement().executeQuery(sql);

            // Ergebnisse ausgeben
            while (result.next()) {
                System.out.println("Gericht: " + result.getString("name"));
                System.out.println("Bewertung: " + result.getInt("rating"));
                System.out.println("Kommentar: " + result.getString("comment"));
                System.out.println("---------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}