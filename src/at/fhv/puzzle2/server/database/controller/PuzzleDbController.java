package at.fhv.puzzle2.server.database.controller;

import at.fhv.puzzle2.server.database.DatabaseConnection;
import at.fhv.puzzle2.server.database.UncheckedSQLException;
import at.fhv.puzzle2.server.database.helper.DBColumnHelper;
import at.fhv.puzzle2.server.database.helper.DbTableHelper;
import at.fhv.puzzle2.server.entity.Puzzle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class PuzzleDbController extends DbController {
    public PuzzleDbController(DatabaseConnection connection) {
        super(connection);
    }

    public List<Puzzle> getPuzzleList() {
        String query = "SELECT * FROM " + DbTableHelper.PUZZLE_TABLE;

        ResultSet result = _connection.executeQuery(query);

        List<Puzzle> puzzleList = new LinkedList<>();
        try {
            while(result.next()) {
                Puzzle puzzle = new Puzzle(result.getInt(DBColumnHelper.ID),
                        result.getString(DBColumnHelper.PUZZLE_NAME));

                puzzleList.add(puzzle);
            }
        } catch (SQLException e) {
            throw new UncheckedSQLException(e);
        }

        return puzzleList;
    }

    public Optional<Puzzle> getPuzzleByName(String name) {
        String query = "SELECT * FROM " + DbTableHelper.PUZZLE_TABLE + " WHERE " + DBColumnHelper.PUZZLE_NAME + " LIKE '" + name + "'";

        ResultSet resultSet = _connection.executeQuery(query);
        try {
            if(resultSet.next()) {
                return Optional.of(new Puzzle(resultSet.getInt(DBColumnHelper.ID), resultSet.getString(DBColumnHelper.PUZZLE_NAME)));
            }
        } catch (SQLException e) {
            throw new UncheckedSQLException(e);
        }

        return Optional.empty();
    }

    /**
     * Stores the puzzle inside the database and sets the ID of the puzzle
     * @param puzzle Puzzle
     */
    public void persistPuzzle(Puzzle puzzle) {
        String query = "INSERT INTO " + DbTableHelper.PUZZLE_TABLE +
                " (" + DBColumnHelper.PUZZLE_NAME + ") VALUES ('" + puzzle.getName() + "')";

        puzzle.setID(_connection.executeSingleUpdateOrInsert(query));
    }

    public Optional<Puzzle> getPuzzleByID(Integer id) {
        String query = "SELECT * FROM " + DbTableHelper.PUZZLE_TABLE +
                " WHERE " + DBColumnHelper.ID + " = " + id;

        ResultSet result = _connection.executeQuery(query);

        try {
            if(result.next()) {
                return Optional.of(new Puzzle(result.getInt(DBColumnHelper.ID), result.getString(DBColumnHelper.PUZZLE_NAME)));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new UncheckedSQLException(e);
        }
    }
}
