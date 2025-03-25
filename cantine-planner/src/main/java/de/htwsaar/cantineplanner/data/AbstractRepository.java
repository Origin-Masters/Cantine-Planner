// File: src/main/java/de/htwsaar/cantineplanner/data/AbstractRepository.java
package de.htwsaar.cantineplanner.data;

import de.htwsaar.cantineplanner.dataAccess.HikariCPDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;

/**
 * The AbstractRepository class is an abstract class that provides common functionality for all repository classes.
 */
public abstract class AbstractRepository {
    protected static HikariCPDataSource dataSource;

    /**
     * Constructs a new AbstractRepository object.
     * @param dataSource an instance of HikariCPDataSource, offering a connection pool
     * for efficient and reliable database connectivity.
     */
    protected AbstractRepository(HikariCPDataSource dataSource) {
        AbstractRepository.dataSource = dataSource;
    }

    /**
     * Retrieves a DSLContext object for the given connection.
     *
     * @param connection the connection to the database
     * @return a DSLContext object for the given connection
     */
    protected DSLContext getDSLContext(Connection connection) {
        return DSL.using(connection, SQLDialect.SQLITE);
    }
}