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

    PuzzlePart _currentPart;

    public PuzzleManager() {
        _random = new Random(System.nanoTime());
        _finishedParts = new LinkedList<>();
    }

    public void setPuzzle(Puzzle puzzle) {
        _puzzle = puzzle;
    }

    public PuzzlePart getNextRandomPuzzlePart() {
        List<PuzzlePart> partList = _puzzle.getPartsList();
        _currentPart = partList.get(_random.nextInt(partList.size() - 1));

        _puzzle.setPuzzlePartList(partList.stream()
                .filter(part -> !(Objects.equals(part.getID(), _currentPart.getID())))
                .collect(toCollection(LinkedList::new)));

        return _currentPart;
    }

    public List<PuzzlePart> getFinishedParts() {
        return _finishedParts;
    }

    public boolean barcodeMatches(String barcode) {
        return Objects.equals(_currentPart.getBarcode(), barcode);
    }

    public boolean barcodeScanned(String barcode) {
        if(!barcodeMatches(barcode)) {
            return false;
        }

        _finishedParts.add(_currentPart);
        _currentPart = null;

        return true;
    }

    public boolean partsAvailable() {
        return _puzzle.getPartsList().size() > 0;
    }
}
