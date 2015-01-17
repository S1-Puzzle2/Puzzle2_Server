package at.fhv.puzzle2.server.database.entity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Question {
    Integer _id;
    String _text;
    List<Answer> _answerList = new LinkedList<>();

    public Question(Integer id, String text) {
        _id = id;
        _text = text;
    }

    public Question(String text) {
        this(null, text);
    }

    public Integer getId() {
        return _id;
    }

    public String getText() {
        return _text;
    }

    public List<Answer> getAnswerList() {
        return _answerList;
    }

    public List<Answer> getShuffledAnswerList() {
        long seed = System.nanoTime();

        List<Answer> shuffledList = new LinkedList<>();

        Collections.copy(shuffledList, _answerList);

        Collections.shuffle(shuffledList, new Random(seed));
        Collections.shuffle(shuffledList, new Random(seed));

        return shuffledList;
    }

    public void addAnswer(Answer answer) {
        _answerList.add(answer);
    }
}
