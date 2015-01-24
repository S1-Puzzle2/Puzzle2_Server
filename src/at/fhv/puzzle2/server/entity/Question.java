package at.fhv.puzzle2.server.entity;

import java.util.*;

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

    public Answer getAnswerByIndex(int index) {
        return _answerList.get(index);
    }

    public void shuffleAnswers() {
        long seed = System.nanoTime();

        Collections.shuffle(_answerList, new Random(seed));
        Collections.shuffle(_answerList, new Random(seed));
    }

    public int getCorrectAnswerIndex() {
        for(int i = 0; i < _answerList.size(); i++) {
            if(_answerList.get(i).isCorrect()) {
                return i;
            }
        }

        return -1;
    }

    public HashMap<Integer, String> getAnswerMap() {
        HashMap<Integer, String> answerMap = new LinkedHashMap<>();
        for(int i = 0; i < _answerList.size(); i++) {
            answerMap.put(i, _answerList.get(i).getText());
        }

        return answerMap;
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
