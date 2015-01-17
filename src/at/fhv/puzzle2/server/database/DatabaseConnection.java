package at.fhv.puzzle2.server.database;

import java.sql.*;

public class DatabaseConnection {
    private Connection _connection;
    private final Object _lock = new Object();

    public DatabaseConnection(Connection connection) {
        _connection = connection;
    }

    public ResultSet executeQuery(String query) throws SQLException {
        synchronized (_lock) {
            Statement statement = _connection.createStatement();
            return statement.executeQuery(query);
        }
    }

    public int executeUpdateOrInsert(String query) throws SQLException {
        synchronized (_lock) {
            Statement statement = _connection.createStatement();
            return statement.executeUpdate(query);
        }
    }

    public PreparedStatement createPreparedStatement(String query) throws SQLException {
        synchronized (_lock) {
            return _connection.prepareStatement(query);
        }
    }
}
