package at.fhv.puzzle2.server.database.entity;

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

    public Puzzle(String name) {
        this(null, name);
    }

    public Integer getId() {
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
}
