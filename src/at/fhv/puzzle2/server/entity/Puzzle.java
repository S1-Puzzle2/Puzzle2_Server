package at.fhv.puzzle2.server.entity;

import java.util.LinkedList;
import java.util.List;

public class Puzzle {
    Integer _id;
    String _name;

    List<PuzzlePart> _partsList = new LinkedList<>();

    public Puzzle(Integer id, String name) {
        _id = id;
        _name = name;
    }

    public Puzzle(Puzzle puzzle) {
        _id = puzzle.getID();
        _name = puzzle.getName();

        _partsList = new LinkedList<>();

        for(PuzzlePart part: puzzle._partsList) {
            _partsList.add(part);
        }
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
