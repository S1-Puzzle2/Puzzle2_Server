package at.fhv.puzzle2.server.logic.manager;

import at.fhv.puzzle2.logging.Logger;
import at.fhv.puzzle2.server.database.Database;
import at.fhv.puzzle2.server.entity.Question;
import at.fhv.puzzle2.server.entity.manager.QuestionEntityManager;

import java.sql.SQLException;
import java.util.*;

import static java.util.stream.Collectors.toCollection;

public class QuestionManager implements Cloneable {
    private static final String TAG = "server.QuestionManager";
    private List<Question> _questionList;
    private Random _random;

    public QuestionManager(List<Question> questionList) {
        _random = new Random(System.nanoTime());

        _questionList = questionList;
    }

    public Optional<Question> getNextRandomQuestion() {
        if(_questionList.size() == 0) {
            //Reload all questions, we dont have any left :O
            try {
                Logger.getLogger().warn(TAG, "Reloaded all question, noone was left...");
                _questionList = new QuestionEntityManager(Database.getInstance()).loadQuestions();
            } catch (SQLException e) {
                e.printStackTrace();
                return Optional.empty();
            }
        }

        Question tmpQuestion = _questionList.get(_random.nextInt(_questionList.size()));

        _questionList = _questionList.stream().
                filter(question -> !Objects.equals(question.getID(), tmpQuestion.getID())).
                collect(toCollection(LinkedList::new));

        tmpQuestion.shuffleAnswers();

        return Optional.of(tmpQuestion);
    }

    @Override
    public Object clone() {
        try {
            QuestionManager questionManager = (QuestionManager) super.clone();
            questionManager._random = new Random(System.nanoTime());

            questionManager._questionList = new LinkedList<>();
            for(Question question: _questionList) {
                questionManager._questionList.add((Question) question.clone());
            }

            return questionManager;

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return null;
    }
}
