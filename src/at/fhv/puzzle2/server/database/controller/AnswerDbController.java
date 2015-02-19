package at.fhv.puzzle2.server.database.controller;

import at.fhv.puzzle2.server.database.DatabaseConnection;
import at.fhv.puzzle2.server.database.UncheckedSQLException;
import at.fhv.puzzle2.server.database.helper.DBColumnHelper;
import at.fhv.puzzle2.server.database.helper.DbTableHelper;
import at.fhv.puzzle2.server.entity.Answer;
import at.fhv.puzzle2.server.entity.Question;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class AnswerDbController extends DbController {
    public AnswerDbController(DatabaseConnection connection) {
        super(connection);
    }

    public List<Answer> getAnswerListByQuestion(int questionID) {
        String query = "SELECT * FROM " + DbTableHelper.ANSWER_TABLE + " WHERE " + DBColumnHelper.ANSWER_QUESTION_REF + " = " + questionID;

        ResultSet result = _connection.executeQuery(query);

        List<Answer> answerList = new LinkedList<>();
        try {
            while(result.next()) {
                Answer answer = new Answer(result.getInt(DBColumnHelper.ID),
                        result.getString(DBColumnHelper.ANSWER_TEXT), result.getBoolean(DBColumnHelper.ANSWER_IS_CORRECT));

                answerList.add(answer);
            }
        } catch (SQLException e) {
            throw new UncheckedSQLException(e);
        }

        return answerList;
    }

    /**
     * Stores a answerlist inside the database and sets the ID of the answer
     * @param answerList List<Answer> List of answer to store inside the database
     * @param questionID Integer ID of the question, the answers belong to
     */
    void persistAnswerList(List<Answer> answerList, Integer questionID) {
        String query = "INSERT INTO " + DbTableHelper.ANSWER_TABLE +
                "(" + DBColumnHelper.ANSWER_TEXT + ", " + DBColumnHelper.ANSWER_IS_CORRECT + ", "
                + DBColumnHelper.ANSWER_QUESTION_REF + ") VALUES (?, ?, ?)";

        PreparedStatement preparedStatement = _connection.createPreparedStatement(query, Statement.RETURN_GENERATED_KEYS);

        try {
            for(Answer answer : answerList) {
                preparedStatement.setString(1, answer.getText());
                preparedStatement.setBoolean(2, answer.isCorrect());
                preparedStatement.setInt(3, questionID);

                answer.setID(preparedStatement.executeUpdate());
            }
        } catch (SQLException e) {
            throw new UncheckedSQLException(e);
        }
    }

    public void peristAnswerListOfQuestion(Question question) throws SQLException {
        persistAnswerList(question.getAnswerList(), question.getID());
    }
}
