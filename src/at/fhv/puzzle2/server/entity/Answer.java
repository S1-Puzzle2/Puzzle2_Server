package at.fhv.puzzle2.server.entity;

public class Answer implements Cloneable {
    private Integer _id;
    private final String _text;
    private final boolean _isCorrect;

    public Answer(Integer id, String text, boolean isCorrect) {
        _id = id;
        _text = text;
        _isCorrect = isCorrect;
    }

    public Answer(String text, boolean isCorrect) {
        this(null, text, isCorrect);
    }

    public String getText() {
        return _text;
    }

    public Integer getID() {
        return _id;
    }

    public boolean isCorrect() {
        return _isCorrect;
    }

    public void setID(int id) {
        _id = id;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
