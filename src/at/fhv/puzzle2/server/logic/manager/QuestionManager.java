package at.fhv.puzzle2.server.logic.manager;

import at.fhv.puzzle2.server.entity.Question;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static java.util.stream.Collectors.toCollection;

public class QuestionManager {
    private List<Question> _questionList;
    private Random _random;

    private Question _currentQuestion;

    public QuestionManager() {
        _random = new Random(System.nanoTime());
    }

    public void setQuestionList(List<Question> questionList) {
        _questionList = questionList;
    }


    public Question getNextRandomQuestion() {
        _currentQuestion = _questionList.get(_random.nextInt(_questionList.size() - 1));

        _questionList = _questionList.stream().
                filter(question -> !Objects.equals(question.getID(), _currentQuestion.getID())).
                collect(toCollection(LinkedList::new));

        _currentQuestion.shuffleAnswers();

        return _currentQuestion;
    }

    public boolean isRightAnswer(int answerIndex) {
        return _currentQuestion.getAnswerList().get(answerIndex).isCorrect();
    }

    public boolean questionsAvailable() {
        return _questionList.size() > 0;
    }
}
