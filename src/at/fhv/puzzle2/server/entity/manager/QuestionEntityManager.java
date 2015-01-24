package at.fhv.puzzle2.server.entity.manager;


import at.fhv.puzzle2.server.database.Database;
import at.fhv.puzzle2.server.database.controller.AnswerDbController;
import at.fhv.puzzle2.server.database.controller.QuestionDbController;
import at.fhv.puzzle2.server.entity.Question;

import java.sql.SQLException;
import java.util.List;

public class QuestionEntityManager extends EntityManager {

    public QuestionEntityManager(Database database) {
        super(database);
    }

    public List<Question> loadQuestions() throws SQLException {
        QuestionDbController questionDbController = _database.getQuestionController();
        AnswerDbController answerDbController = _database.getAnswerController();

        List<Question> questionList = questionDbController.getQuestionList();

        for(Question question: questionList) {
            question.setAnswerList(answerDbController.getAnswerListByQuestion(question.getID()));
        }

        return questionList;
    }

    public void storeQuestion(Question question) throws SQLException {
        QuestionDbController questionDbController = _database.getQuestionController();
        AnswerDbController answerDbController = _database.getAnswerController();

        questionDbController.persistQuestion(question);
        answerDbController.peristAnswerListOfQuestion(question);
    }
}
