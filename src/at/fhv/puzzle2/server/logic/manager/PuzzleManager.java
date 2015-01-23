package at.fhv.puzzle2.server.logic.manager;

import at.fhv.puzzle2.server.entity.Puzzle;
import at.fhv.puzzle2.server.entity.PuzzlePart;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static java.util.stream.Collectors.toCollection;

public class PuzzleManager {
    Puzzle _puzzle;
    Random _random;

    List<PuzzlePart> _finishedParts;

    public PuzzleManager(Puzzle puzzle) {
        _random = new Random(System.nanoTime());
        _finishedParts = new LinkedList<>();

        _puzzle = puzzle;
    }

    public PuzzlePart getNextRandomPuzzlePart() {
        List<PuzzlePart> partList = _puzzle.getPartsList();
        PuzzlePart nextPart = partList.get(_random.nextInt(partList.size() - 1));

        _puzzle.setPuzzlePartList(partList.stream()
                .filter(part -> !(Objects.equals(part.getID(), nextPart.getID())))
                .collect(toCollection(LinkedList::new)));

        return nextPart;
    }

    public void partFinished(PuzzlePart part) {
        _finishedParts.add(part);
    }

    public List<PuzzlePart> getFinishedParts() {
        return _finishedParts;
    }

    public boolean partsAvailable() {
        return _puzzle.getPartsList().size() > 0;
    }
}
