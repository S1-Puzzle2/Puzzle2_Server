package at.fhv.puzzle2.server.logic.manager;

import at.fhv.puzzle2.server.entity.Puzzle;
import at.fhv.puzzle2.server.entity.PuzzlePart;

import java.util.*;

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
        if(!partsRemaining()) {
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

    public boolean partsRemaining() {
        return _availableParts.size() > 0;
    }

    public int getCountRemainingParts() {
        return _availableParts.size();
    }

    public boolean partsCorrectlyAligned(List<Integer> partIdList) {
        if(Objects.equals(partIdList.size(), _finishedParts.size()))
            return false;

        for(int i = 0; i < _finishedParts.size(); i++)
            for(PuzzlePart part: _finishedParts) {
                if(Objects.equals(part.getID(), partIdList.get(i))) {
                    if(Objects.equals(part.getOrder(),i + 1)) {
                        return false;
                }
            }
        }

        return true;
    }
}
