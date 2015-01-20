package at.fhv.puzzle2.server.database.controller;

import at.fhv.puzzle2.server.database.DatabaseConnection;
import at.fhv.puzzle2.server.database.helper.DBColumnHelper;
import at.fhv.puzzle2.server.database.helper.DbTableHelper;
import at.fhv.puzzle2.server.entity.Question;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class QuestionDbController extends DbController {
    public QuestionDbController(DatabaseConnection connection) {
        super(connection);
    }

    /**
     * Stores a question inside the database and sets the ID of the question automatically
     * @param question ShowQuestion The question to store
     * @throws SQLException
     */
    public void persistQuestion(Question question) throws SQLException {
        String query = "INSERT INTO " + DbTableHelper.QUESTION_TABLE +
                " (" + DBColumnHelper.QUESTION_TEXT + ") VALUES (" + question.getText() + ")";

        question.setID(_connection.executeSingleUpdateOrInsert(query));
    }

    public List<Question> getQuestionList() throws SQLException {
        String query = "SELECT * FROM " + DbTableHelper.QUESTION_TABLE;

        ResultSet result = _connection.executeQuery(query);

        List<Question> questionList = new LinkedList<>();
        while(result.next()) {
            Question question = new Question(result.getInt(DBColumnHelper.ID),
                    result.getString(DBColumnHelper.QUESTION_TEXT));

            questionList.add(question);
        }

        return questionList;
    }
}
