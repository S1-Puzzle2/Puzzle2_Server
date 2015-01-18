package at.fhv.puzzle2.server.entity.manager;

import at.fhv.puzzle2.server.database.Database;
import at.fhv.puzzle2.server.database.controller.PuzzleDbController;
import at.fhv.puzzle2.server.database.controller.PuzzlePartDbController;
import at.fhv.puzzle2.server.entity.Puzzle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class PuzzleEntityManager extends EntityManager {
    protected PuzzleEntityManager(Database database) {
        super(database);
    }

    public List<Puzzle> getPuzzleList() throws SQLException, IOException {
        PuzzleDbController puzzleDbController = _database.getPuzzleController();
        PuzzlePartDbController puzzlePartDbController = _database.getPuzzlePartController();

        List<Puzzle> puzzleList = puzzleDbController.getPuzzleList();

        for(Puzzle puzzle : puzzleList) {
            puzzle.setPuzzlePartList(puzzlePartDbController.getPuzzlePartsByPuzzle(puzzle));
        }

        return puzzleList;
    }

    public Puzzle getPuzzleByID(Integer id) throws SQLException, IOException {
        PuzzleDbController puzzleDbController = _database.getPuzzleController();
        PuzzlePartDbController puzzlePartDbController = _database.getPuzzlePartController();

        Puzzle puzzle = puzzleDbController.getPuzzleByID(id);

        if(puzzle == null) {
            return null;
        }

        puzzle.setPuzzlePartList(puzzlePartDbController.getPuzzlePartsByPuzzle(puzzle));

        return puzzle;
    }

    public void storePuzzle(Puzzle puzzle) throws SQLException {
        PuzzleDbController puzzleDbController = _database.getPuzzleController();
        PuzzlePartDbController puzzlePartDbController = _database.getPuzzlePartController();

        puzzleDbController.persistPuzzle(puzzle);
        puzzlePartDbController.persistPuzzlePartList(puzzle.getPartsList(), puzzle);
    }
}
