package de.htwsaar.cantineplanner.dataAccess;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class DBConnection {
    private DSLContext create;
    private String dbUrl = "jdbc:sqlite:src/main/resources/database.db"; // Pfad zur SQLite-Datenbank


    public DBConnection() {
        try {
            System.out.println("Starte Anwendung...");
            DBService dbService = new DBService();

            // Beispiel-Query ausführen
            dbService.fetchAllMeals();
        } finally {
            // Pool sauber schließen
            HikariCPDataSource.closeDataSource();
            System.out.println("Datenbankverbindung geschlossen.");
        }

        // JOOQ Connection


    }
}
