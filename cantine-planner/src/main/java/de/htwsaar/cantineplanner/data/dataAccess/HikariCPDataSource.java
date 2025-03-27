package de.htwsaar.cantineplanner.data.dataAccess;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class HikariCPDataSource {

    private final HikariDataSource dataSource;

    /**
     * Load the HikariCP configuration from hikari.properties and create the data source pool
     */
    public HikariCPDataSource( ) {
        String PATH_TO_PROPERTIES = "hikari.properties";
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(PATH_TO_PROPERTIES)) {
            if (input == null) {
                throw new RuntimeException("Unable to find " + PATH_TO_PROPERTIES + " in classpath.");
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
 * Constructs a HikariCPDataSource with the specified properties file path.
 *
 * @param pathToProperties the path to the properties file
 * @throws RuntimeException if the properties file cannot be found or an error occurs while loading the properties
 */
public HikariCPDataSource(String pathToProperties) {

    try (InputStream input = getClass().getClassLoader().getResourceAsStream(pathToProperties)) {
        if (input == null) {
            throw new RuntimeException("Unable to find " + pathToProperties + " in classpath.");
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