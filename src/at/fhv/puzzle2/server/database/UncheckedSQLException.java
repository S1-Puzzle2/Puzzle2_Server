package at.fhv.puzzle2.server.database;

import java.sql.SQLException;

public class UncheckedSQLException extends RuntimeException {
    public UncheckedSQLException(SQLException exception) {
        super(exception);
    }
}
