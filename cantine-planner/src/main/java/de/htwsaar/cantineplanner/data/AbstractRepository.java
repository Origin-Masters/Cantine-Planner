// File: src/main/java/de/htwsaar/cantineplanner/data/AbstractRepository.java
package de.htwsaar.cantineplanner.data;

import de.htwsaar.cantineplanner.dataAccess.HikariCPDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractRepository {
    protected static HikariCPDataSource dataSource;


    protected AbstractRepository(HikariCPDataSource dataSource) {
        AbstractRepository.dataSource = dataSource;
    }

    protected Connection getDataSource() throws SQLException {
        return dataSource.getConnection();
    }

    protected DSLContext getDSLContext(Connection connection) {
        return DSL.using(connection, SQLDialect.SQLITE);
    }

}