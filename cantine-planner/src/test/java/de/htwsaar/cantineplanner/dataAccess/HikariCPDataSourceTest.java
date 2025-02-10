package de.htwsaar.cantineplanner.dataAccess;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

import java.sql.Connection;


public class HikariCPDataSourceTest {
    private HikariCPDataSource hikariCPDataSource;
    String PATH_TO_TEST_PROPERTIES = "src/test/resources/hikari-test.properties";

    @Before
    public void setUp() {
        hikariCPDataSource = new HikariCPDataSource(PATH_TO_TEST_PROPERTIES);
    }

    @After
    public void tearDown() {
        hikariCPDataSource.closeDataSource();
    }

    // Test case to check if the connection is established
    @Test
    public void testGetConnection() {
        try (Connection connection = hikariCPDataSource.getConnection()) {
            assertNotNull("Connection should not be null", connection);
            assertFalse("Connection should be open", connection.isClosed());
        } catch (SQLException e) {
            fail("SQLException occurred: " + e.getMessage());
        }
    }

    // Test case to check if the connection is closed
    @Test
    public void testCloseDataSource() {
        hikariCPDataSource.closeDataSource();
        try (Connection connection = hikariCPDataSource.getConnection()) {
            fail("Should not be able to get connection after data source is closed");
        } catch (SQLException e) {
            assertNotNull("SQLException should not be null", e);
        }
    }


}
