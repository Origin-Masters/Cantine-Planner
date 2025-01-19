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

    /**
     * Load the HikariCP configuration from hikari.properties and create the data source pool
     */
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

    /**
     * Private constructor to prevent instantiation
     */
    private HikariCPDataSource() {
    }

    /**
     * Get a connection from the data source
     *
     * @return Connection from the data source
     * @throws SQLException If a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Close the data source
     */
    public static void closeDataSource() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}