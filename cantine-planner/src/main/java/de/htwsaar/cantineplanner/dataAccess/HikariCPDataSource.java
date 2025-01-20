package de.htwsaar.cantineplanner.dataAccess;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class HikariCPDataSource {

    private HikariDataSource dataSource;

    /**
     * Load the HikariCP configuration from hikari.properties and create the data source pool
     */
    public HikariCPDataSource() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("hikari.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find hikari.properties");
            }

            Properties properties = new Properties();
            properties.load(input);

            HikariConfig config = new HikariConfig(properties);
            this.dataSource = new HikariDataSource(config);
        } catch (IOException e) {
            throw new RuntimeException("Error loading HikariCP configuration", e);
        }
    }

    /**
     * Get a connection from the data source
     *
     * @return Connection from the data source
     * @throws SQLException If a database access error occurs
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Close the data source
     */
    public void closeDataSource() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}