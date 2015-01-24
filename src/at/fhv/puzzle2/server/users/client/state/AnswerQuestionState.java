package at.fhv.puzzle2.server.users.client.state;

import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.command.commands.mobile.AnswerCorrectCommand;
import at.fhv.puzzle2.communication.application.command.commands.mobile.QuestionAnsweredCommand;
import at.fhv.puzzle2.communication.application.command.commands.mobile.ShowQuestionCommand;
import at.fhv.puzzle2.server.SendQueue;
import at.fhv.puzzle2.server.entity.Answer;
import at.fhv.puzzle2.server.entity.PuzzlePart;
import at.fhv.puzzle2.server.entity.Question;
import at.fhv.puzzle2.server.users.client.Client;


public class AnswerQuestionState extends ClientState {
    private Question _question;
    private final PuzzlePart _puzzlePart;
    public AnswerQuestionState(Client client, PuzzlePart puzzlePart) {
        super(client);

        _puzzlePart = puzzlePart;
    }

    public void setQuestion(Question question) {
        _question = question;
    }

    public PuzzlePart getPuzzlePart() {
        return _puzzlePart;
    }

    @Override
    public ClientState handleCommand(Command command) {
        if(!(command instanceof QuestionAnsweredCommand)) {
            return null;
        }

        AnswerCorrectCommand response = new AnswerCorrectCommand(command.getClientID());
        response.setConnection(_client.getConnection());

        Answer answer = _question.getAnswerByIndex(((QuestionAnsweredCommand) command).getAnswerID());

        response.setIsCorrect(answer.isCorrect());
        response.setCorrectAnswerID(_question.getCorrectAnswerIndex());

        SendQueue.getInstance().addCommandToSend(response);

        if(answer.isCorrect()) {
            return new SearchPartClientState(_client);
        }

        return new AnswerQuestionState(_client, this._puzzlePart);
    }

    @Override
    public void enter() {
        ShowQuestionCommand command = new ShowQuestionCommand(_client.getClientID());
        command.setConnection(_client.getConnection());

        command.setQuestionID(_question.getID());
        command.setQuestionText(_question.getText());


        command.setAnswerMap(_question.getAnswerMap());

        SendQueue.getInstance().addCommandToSend(command, 3000);
    }
}
