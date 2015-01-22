package at.fhv.puzzle2.server.logic.manager;

import at.fhv.puzzle2.server.entity.Question;
import at.fhv.puzzle2.server.entity.manager.QuestionEntityManager;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static java.util.stream.Collectors.toCollection;

public class QuestionManager {
    private List<Question> _questionList;
    private Random _random;

    public QuestionManager(List<Question> questionList) {
        _random = new Random(System.nanoTime());

        _questionList = questionList;
    }


    public Question getNextRandomQuestion() {
        Question tmpQuestion = _questionList.get(_random.nextInt(_questionList.size() - 1));

        _questionList = _questionList.stream().
                filter(question -> !Objects.equals(question.getID(), tmpQuestion.getID())).
                collect(toCollection(LinkedList::new));

        tmpQuestion.shuffleAnswers();

        return tmpQuestion;
    }
}
