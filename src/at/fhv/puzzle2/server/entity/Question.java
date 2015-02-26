package at.fhv.puzzle2.server.entity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Question implements Cloneable {
    private Integer _id;
    private final String _text;
    private List<Answer> _answerList = new LinkedList<>();

    public Question(Integer id, String text) {
        _id = id;
        _text = text;
    }

    public Question(String text) {
        this(null, text);
    }

    public Integer getID() {
        return _id;
    }

    public void setID(Integer id) {
        _id = id;
    }

    public String getText() {
        return _text;
    }

    public List<Answer> getAnswerList() {
        return _answerList;
    }

    public Answer getAnswerById(int id) {
        return _answerList.stream().filter(answer -> answer.getID() == id).findFirst().get();
    }

    public void shuffleAnswers() {
        long seed = System.nanoTime();

        Collections.shuffle(_answerList, new Random(seed));
        Collections.shuffle(_answerList, new Random(seed));
    }

    public int getCorrectAnswerID() {
        for (Answer a_answerList : _answerList) {
            if (a_answerList.isCorrect()) {
                return a_answerList.getID();
            }
        }

        return -1;
    }

    void addAnswer(Answer answer) {
        _answerList.add(answer);
    }

    public void setAnswerList(List<Answer> answerList) {
        _answerList = answerList;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Question question = (Question) super.clone();
        question._answerList = new LinkedList<>();

        for(Answer answer: _answerList) {
            question.addAnswer((Answer) answer.clone());
        }

        return question;
    }
}
