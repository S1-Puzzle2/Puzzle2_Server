package at.fhv.puzzle2.server.logic.manager;

import at.fhv.puzzle2.server.entity.Puzzle;
import at.fhv.puzzle2.server.entity.PuzzlePart;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static java.util.stream.Collectors.toCollection;

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

    public PuzzlePart getNextRandomPuzzlePart() {
        if(!partsAvailable()) {
            return null;
        }

        PuzzlePart nextPart = _availableParts.get(_random.nextInt(_availableParts.size()));

        //Remove it from the list nw
        _availableParts.remove(nextPart);

        return nextPart;
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
