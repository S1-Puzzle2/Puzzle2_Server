package at.fhv.puzzle2.server.entity.manager;

import at.fhv.puzzle2.server.database.Database;
import at.fhv.puzzle2.server.database.controller.PuzzleDbController;
import at.fhv.puzzle2.server.database.controller.PuzzlePartDbController;
import at.fhv.puzzle2.server.entity.Puzzle;
import at.fhv.puzzle2.server.entity.PuzzlePart;

import java.util.List;
import java.util.Optional;

public class PuzzleEntityManager extends EntityManager {
    public PuzzleEntityManager(Database database) {
        super(database);
    }

    public List<Puzzle> getPuzzleList() {
        PuzzleDbController puzzleDbController = _database.getPuzzleController();
        PuzzlePartDbController puzzlePartDbController = _database.getPuzzlePartController();

        List<Puzzle> puzzleList = puzzleDbController.getPuzzleList();

        for(Puzzle puzzle : puzzleList) {
            puzzle.setPuzzlePartList(puzzlePartDbController.getPuzzlePartsByPuzzle(puzzle));
        }

        return puzzleList;
    }

    public Optional<Puzzle> getPuzzleByID(Integer id) {
        PuzzleDbController puzzleDbController = _database.getPuzzleController();
        PuzzlePartDbController puzzlePartDbController = _database.getPuzzlePartController();

        Optional<Puzzle> puzzle = puzzleDbController.getPuzzleByID(id);

        if(!puzzle.isPresent()) {
            return Optional.empty();
        }

        puzzle.get().setPuzzlePartList(puzzlePartDbController.getPuzzlePartsByPuzzle(puzzle.get()));

        return Optional.of(puzzle.get());
    }

    public void storePuzzle(Puzzle puzzle) {
        PuzzleDbController puzzleDbController = _database.getPuzzleController();
        PuzzlePartDbController puzzlePartDbController = _database.getPuzzlePartController();

        puzzleDbController.persistPuzzle(puzzle);
    }

    public void storePuzzlePart(PuzzlePart puzzlePart, int puzzleID) {
        PuzzleDbController puzzleDbController = Database.getInstance().getPuzzleController();
        PuzzlePartDbController puzzlePartDbController = Database.getInstance().getPuzzlePartController();

        Optional<Puzzle> puzzle = puzzleDbController.getPuzzleByID(puzzleID);
        puzzlePartDbController.persistPuzzlePart(puzzlePart, puzzle.get());
    }

    public Optional<Puzzle> getPuzzleByName(String puzzleName) {
        PuzzleDbController puzzleDbController = _database.getPuzzleController();
        PuzzlePartDbController puzzlePartDbController = _database.getPuzzlePartController();

        Optional<Puzzle> puzzle = puzzleDbController.getPuzzleByName(puzzleName);
        if(puzzle.isPresent()) {
            puzzle.get().setPuzzlePartList(puzzlePartDbController.getPuzzlePartsByPuzzle(puzzle.get()));
        }

        return puzzle;
    }

    public Optional<PuzzlePart> getPuzzlePartByID(Integer id) {
        PuzzlePartDbController partDbController = _database.getPuzzlePartController();
        return partDbController.getPuzzlePartByID(id);
    }
}
