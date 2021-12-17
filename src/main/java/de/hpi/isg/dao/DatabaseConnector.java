package de.hpi.isg.dao;

import lombok.Getter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manage the access to a database by maintaining batch writers and ensuring all data is written before performing a
 * read.
 *
 * @author Lan Jiang
 * @since 9/9/19
 */
public class DatabaseConnector {

    /**
     * The connection to the database.
     */
    private Connection connection;

    public DatabaseConnector() {
        runCreateDatabaseScript();
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/line_function_db",
                    "Fuga", null);
            this.connection.setAutoCommit(false);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("org.postgresql.Driver");
                this.connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/line_function_db",
                        "Fuga", null);
                this.connection.setAutoCommit(false);
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return connection;
    }

    private void runCreateDatabaseScript() {
        try {
            Runtime.getRuntime().exec("./src/main/resources/create.sh");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
