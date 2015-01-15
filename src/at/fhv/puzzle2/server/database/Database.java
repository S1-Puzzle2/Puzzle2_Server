package at.fhv.puzzle2.server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private Connection _connection;

    public Database(String dsn, String user, String password) throws SQLException {
        _connection = DriverManager.getConnection(dsn, user, password);
    }
}
