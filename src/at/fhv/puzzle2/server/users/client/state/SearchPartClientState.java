package at.fhv.puzzle2.server.users.client.state;

import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.command.commands.mobile.BarCodeCorrectCommand;
import at.fhv.puzzle2.communication.application.command.commands.mobile.PartScannedCommand;
import at.fhv.puzzle2.communication.application.command.commands.mobile.SearchPuzzlePartCommand;
import at.fhv.puzzle2.server.SendQueue;
import at.fhv.puzzle2.server.entity.PuzzlePart;
import at.fhv.puzzle2.server.users.client.Client;

import java.util.Objects;
import java.util.Optional;

public class SearchPartClientState extends ClientState {
    private PuzzlePart _puzzlePart;

    public SearchPartClientState(Client client) {
        super(client);

    }

    @Override
    public Optional<ClientState> handleCommand(Command command) {
        if(!(command instanceof PartScannedCommand)) {
            return Optional.empty();
        }

        PartScannedCommand partScannedCommand = (PartScannedCommand) command;
        boolean correctBarCode = Objects.equals(partScannedCommand.getBarCode(), _puzzlePart.getBarcode());

        BarCodeCorrectCommand barCodeCorrectCommand = new BarCodeCorrectCommand(_client.getClientID());
        barCodeCorrectCommand.setConnection(_client.getConnection());
        barCodeCorrectCommand.setIsCorrect(correctBarCode);

        SendQueue.getInstance().addCommandToSend(barCodeCorrectCommand);

        if(correctBarCode) {
            if(_client.getTeam().getQuestionManager().questionsRemaining()) {
                return Optional.of(new AnswerQuestionState(_client, _puzzlePart));
            } else {
                return Optional.of(new NoQuestionsLeftState(_client));
            }
        }

        return Optional.empty();
    }

    @Override
    public void enter() {
        if(_puzzlePart == null) {
            _puzzlePart = _client.getTeam().getPuzzleManager().getNextRandomPuzzlePart().get();
        }

        SearchPuzzlePartCommand command = new SearchPuzzlePartCommand(_client.getClientID());
        command.setConnection(_client.getConnection());
        command.setPartID(_puzzlePart.getID());

        SendQueue.getInstance().addCommandToSend(command, 3000);
    }

    @Override
    public String toString() {
        return "SearchPart";
    }
}
