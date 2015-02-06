package at.fhv.puzzle2.server.logic.manager;

import at.fhv.puzzle2.server.entity.Puzzle;
import at.fhv.puzzle2.server.entity.PuzzlePart;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class PuzzleManager {
    private final List<PuzzlePart> _availableParts;
    private final Random _random;

    private final List<PuzzlePart> _finishedParts;

    public PuzzleManager(Puzzle puzzle) {
        _random = new Random(System.nanoTime());
        _finishedParts = new LinkedList<>();

        _availableParts = new LinkedList<>();
        _availableParts.addAll(puzzle.getPartsList());
    }

    public Optional<PuzzlePart> getNextRandomPuzzlePart() {
        if(!partsAvailable()) {
            return Optional.empty();
        }

        PuzzlePart nextPart = _availableParts.get(_random.nextInt(_availableParts.size()));

        //Remove it from the list nw
        _availableParts.remove(nextPart);

        return Optional.of(nextPart);
    }

    public void partFinished(PuzzlePart part) {
        _finishedParts.add(part);
    }

    public List<PuzzlePart> getFinishedParts() {
        return _finishedParts;
    }

    public boolean partsAvailable() {
        return _availableParts.size() > 0;
    }
}
