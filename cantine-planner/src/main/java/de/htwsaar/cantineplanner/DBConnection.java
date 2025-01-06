package de.htwsaar.cantineplanner;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {


    private DSLContext create;

    public DBConnection() {
        try {
            // Update the URL to point to the correct directory
            String url = "jdbc:sqlite:/MensaWebseite/database.db";
            Connection connection = DriverManager.getConnection(url);
            this.create = DSL.using(connection, SQLDialect.SQLITE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DSLContext getCreate() {
        return create;
    }

    // JOOQ Connection



}
