package de.htwsaar.cantineplanner.dataAccess;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class HikariCPDataSource {

    private static HikariDataSource dataSource;

    // Static initialization block
    static {
        try (InputStream input = HikariCPDataSource.class.getClassLoader().getResourceAsStream("hikari.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find hikari.properties");
            }

            Properties properties = new Properties();
            properties.load(input);

            HikariConfig config = new HikariConfig(properties);
            dataSource = new HikariDataSource(config);
        } catch (IOException e) {
            throw new RuntimeException("Error loading HikariCP configuration", e);
        }
    }

    // Private constructor to prevent instantiation
    private HikariCPDataSource() {}

    // Get connection from the pool
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // Close the data source
    public static void closeDataSource() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}