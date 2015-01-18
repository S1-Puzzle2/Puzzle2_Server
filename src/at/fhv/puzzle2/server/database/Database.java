package at.fhv.puzzle2.server.database;

import at.fhv.puzzle2.server.database.controller.AnswerDbController;
import at.fhv.puzzle2.server.database.controller.PuzzleDbController;
import at.fhv.puzzle2.server.database.controller.PuzzlePartDbController;
import at.fhv.puzzle2.server.database.controller.QuestionDbController;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private DatabaseConnection _connection;

    public Database(String dsn, String user, String password) throws SQLException {
        _connection = new DatabaseConnection(DriverManager.getConnection(dsn, user, password));
    }

    public QuestionDbController getQuestionController() {
        return new QuestionDbController(_connection);
    }

    public AnswerDbController getAnswerController() {
        return new AnswerDbController(_connection);
    }

    public PuzzleDbController getPuzzleController() {
        return new PuzzleDbController(_connection);
    }

    public PuzzlePartDbController getPuzzlePartController() {
        return new PuzzlePartDbController(_connection);
    }
}
