package de.htwsaar.cantineplanner.dataAccess;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.net.URL;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;

public class HikariCPDataSource {

    private static HikariDataSource dataSource;

    // Statischer Initialisierungsblock
    static {
        try {
            HikariConfig config = new HikariConfig();

            // Lade die Datenbankdatei über den Klassenpfad
            URL resource = HikariCPDataSource.class.getClassLoader().getResource("database.db");
            if (resource == null) {
                throw new IllegalArgumentException("Datenbankdatei nicht im Klassenpfad gefunden!");
            }

            // Absoluten Pfad zur Datenbank generieren
            String dbPath = Paths.get(resource.toURI()).toString();
            config.setJdbcUrl("jdbc:sqlite:" + dbPath);

            // HikariCP-Konfiguration
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(600000);
            config.setMaxLifetime(1800000);
            config.setConnectionTimeout(30000);

            // Datenquelle erstellen
            dataSource = new HikariDataSource(config);

        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Initialisieren des HikariCP-Pools", e);
        }
    }

    // Privater Konstruktor, um Instanziierung zu verhindern
    private HikariCPDataSource() {}

    // Verbindung aus dem Pool abrufen
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // Datenquelle schließen
    public static void closeDataSource() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
