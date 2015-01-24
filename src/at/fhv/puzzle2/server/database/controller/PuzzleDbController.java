package at.fhv.puzzle2.server.database.controller;

import at.fhv.puzzle2.server.database.DatabaseConnection;
import at.fhv.puzzle2.server.database.helper.DBColumnHelper;
import at.fhv.puzzle2.server.database.helper.DbTableHelper;
import at.fhv.puzzle2.server.entity.Puzzle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class PuzzleDbController extends DbController {
    public PuzzleDbController(DatabaseConnection connection) {
        super(connection);
    }

    public List<Puzzle> getPuzzleList() throws SQLException {
        String query = "SELECT * FROM " + DbTableHelper.PUZZLE_TABLE;

        ResultSet result = _connection.executeQuery(query);

        List<Puzzle> puzzleList = new LinkedList<>();
        while(result.next()) {
            Puzzle puzzle = new Puzzle(result.getInt(DBColumnHelper.ID),
                    result.getString(DBColumnHelper.PUZZLE_NAME));

            puzzleList.add(puzzle);
        }

        return puzzleList;
    }

    public Puzzle getPuzzleByName(String name) throws SQLException {
        String query = "SELECT * FROM " + DbTableHelper.PUZZLE_TABLE + " WHERE " + DBColumnHelper.PUZZLE_NAME + " LIKE '" + name + "'";

        ResultSet resultSet = _connection.executeQuery(query);
        if(resultSet.next()) {
            return new Puzzle(resultSet.getInt(DBColumnHelper.ID), resultSet.getString(DBColumnHelper.PUZZLE_NAME));
        }

        return null;
    }

    /**
     * Stores the puzzle inside the database and sets the ID of the puzzle
     * @param puzzle
     * @throws SQLException
     */
    public void persistPuzzle(Puzzle puzzle) throws SQLException {
        String query = "INSERT INTO " + DbTableHelper.PUZZLE_TABLE +
                " (" + DBColumnHelper.PUZZLE_NAME + ") VALUES ('" + puzzle.getName() + "')";

        puzzle.setID(_connection.executeSingleUpdateOrInsert(query));
    }

    public Puzzle getPuzzleByID(Integer id) throws SQLException {
        String query = "SELECT * FROM " + DbTableHelper.PUZZLE_TABLE +
                " WHERE " + DBColumnHelper.ID + " = " + id;

        ResultSet result = _connection.executeQuery(query);

        if(result.next()) {
            return new Puzzle(result.getInt(DBColumnHelper.ID), result.getString(DBColumnHelper.PUZZLE_NAME));
        } else {
            return null;
        }
    }
}
