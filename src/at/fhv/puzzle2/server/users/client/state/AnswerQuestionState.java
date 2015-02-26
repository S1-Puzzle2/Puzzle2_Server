package at.fhv.puzzle2.server.users.client.state;

import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.command.commands.mobile.AnswerCorrectCommand;
import at.fhv.puzzle2.communication.application.command.commands.mobile.AnswerQuestionCommand;
import at.fhv.puzzle2.communication.application.command.commands.mobile.QuestionAnsweredCommand;
import at.fhv.puzzle2.server.SendQueue;
import at.fhv.puzzle2.server.dto_factory.AnswerDTOFactory;
import at.fhv.puzzle2.server.entity.Answer;
import at.fhv.puzzle2.server.entity.PuzzlePart;
import at.fhv.puzzle2.server.entity.Question;
import at.fhv.puzzle2.server.users.client.Client;

import java.util.Optional;

import static java.util.stream.Collectors.toList;


public class AnswerQuestionState extends ClientState {
    private Question _question;
    private final PuzzlePart _puzzlePart;
    public AnswerQuestionState(Client client, PuzzlePart puzzlePart) {
        super(client);

        _puzzlePart = puzzlePart;
    }

    public PuzzlePart getPuzzlePart() {
        return _puzzlePart;
    }

    @Override
    public Optional<ClientState> handleCommand(Command command) {
        if(!(command instanceof QuestionAnsweredCommand)) {
            return Optional.empty();
        }

        AnswerCorrectCommand response = new AnswerCorrectCommand(command.getClientID());
        response.setConnection(_client.getConnection());

        Answer answer = _question.getAnswerById(((QuestionAnsweredCommand) command).getAnswerID());

        response.setIsCorrect(answer.isCorrect());
        response.setCorrectAnswerID(_question.getCorrectAnswerID());

        SendQueue.getInstance().addCommandToSend(response);

        if(answer.isCorrect()) {
            return Optional.of(new SearchPartClientState(_client));
        }

        return Optional.of(new AnswerQuestionState(_client, this._puzzlePart));
    }

    @Override
    public void enter() {
        _question = _client.getTeam().getQuestionManager().getNextRandomQuestion().get();

        AnswerQuestionCommand command = new AnswerQuestionCommand(_client.getClientID());
        command.setConnection(_client.getConnection());

        command.setQuestionID(_question.getID());
        command.setQuestionText(_question.getText());


        command.setAnswerList(_question.getAnswerList().stream().map(AnswerDTOFactory::createAnswer).collect(toList()));

        SendQueue.getInstance().addCommandToSend(command, 3000);
    }

    @Override
    public String toString() {
        return "AnswerQuestion";
    }
}
