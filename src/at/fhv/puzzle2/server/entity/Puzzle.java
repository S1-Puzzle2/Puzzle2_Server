package at.fhv.puzzle2.server.entity;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Puzzle {
    private Integer _id;
    private final String _name;

    private List<PuzzlePart> _partsList = new LinkedList<>();

    public Puzzle(Integer id, String name) {
        _id = id;
        _name = name;
    }

    public Puzzle(Puzzle puzzle) {
        _id = puzzle.getID();
        _name = puzzle.getName();

        _partsList = puzzle._partsList.stream().collect(Collectors.toList());
    }

    public Puzzle(String name) {
        this(null, name);
    }

    public Integer getID() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public List<PuzzlePart> getPartsList() {
        return _partsList;
    }

    public void addPuzzlePart(PuzzlePart part) {
        _partsList.add(part);
    }

    public void setPuzzlePartList(List<PuzzlePart> partList) {
        _partsList = partList;
    }

    public void setID(int id) {
        this._id = id;
    }
}
