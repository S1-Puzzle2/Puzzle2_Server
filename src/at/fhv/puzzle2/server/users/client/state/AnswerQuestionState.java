package at.fhv.puzzle2.server.users.client.state;

import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.command.commands.mobile.AnswerCorrectCommand;
import at.fhv.puzzle2.communication.application.command.commands.mobile.QuestionAnsweredCommand;
import at.fhv.puzzle2.server.SendQueue;
import at.fhv.puzzle2.server.users.Team;
import at.fhv.puzzle2.server.users.client.Client;
import at.fhv.puzzle2.server.entity.Answer;
import at.fhv.puzzle2.server.entity.Question;

public class AnswerQuestionState extends ClientState {
    private Question _question;

    public AnswerQuestionState(Client client) {
        super(client);
    }

    public void setQuestion(Question question) {
        _question = question;
    }

    @Override
    public ClientState handleCommand(Command command) {
        if(!(command instanceof QuestionAnsweredCommand)) {
            return null;
        }

        AnswerCorrectCommand response = new AnswerCorrectCommand(command.getClientID());
        Answer answer = _question.getAnswerByIndex(((QuestionAnsweredCommand) command).getAnswerID());

        response.setIsCorrect(answer.isCorrect());

        SendQueue.getInstance().addCommandToSend(response);

        if(answer.isCorrect()) {
            return new SearchPartClientState(_client);
        }

        return null;
    }
}
