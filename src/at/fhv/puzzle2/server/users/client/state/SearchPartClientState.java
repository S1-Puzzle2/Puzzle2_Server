package at.fhv.puzzle2.server.users.client.state;

import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.command.commands.mobile.BarCodeCorrectCommand;
import at.fhv.puzzle2.communication.application.command.commands.mobile.BarcodeScannedCommand;
import at.fhv.puzzle2.communication.application.command.commands.mobile.SearchPuzzlePartCommand;
import at.fhv.puzzle2.server.SendQueue;
import at.fhv.puzzle2.server.entity.PuzzlePart;
import at.fhv.puzzle2.server.users.client.Client;

import java.util.Objects;

public class SearchPartClientState extends ClientState {
    private PuzzlePart _puzzlePart;

    public SearchPartClientState(Client client) {
        super(client);
    }

    public void setPuzzlePart(PuzzlePart puzzlePart) {
        _puzzlePart = puzzlePart;
    }

    @Override
    public ClientState handleCommand(Command command) {
        if(!(command instanceof BarcodeScannedCommand)) {
            return null;
        }

        BarcodeScannedCommand barcodeScannedCommand = (BarcodeScannedCommand) command;
        boolean correctBarCode = Objects.equals(barcodeScannedCommand.getBarCode(), _puzzlePart.getBarcode());

        BarCodeCorrectCommand barCodeCorrectCommand = new BarCodeCorrectCommand(_client.getClientID());
        barCodeCorrectCommand.setConnection(_client.getConnection());
        barCodeCorrectCommand.setIsCorrect(correctBarCode);

        SendQueue.getInstance().addCommandToSend(barCodeCorrectCommand);

        if(correctBarCode) {
            return new AnswerQuestionState(_client, _puzzlePart);
        }

        return null;
    }

    @Override
    public void enter() {
        SearchPuzzlePartCommand command = new SearchPuzzlePartCommand(_client.getClientID());
        command.setConnection(_client.getConnection());
        command.setPartID(_puzzlePart.getID());

        SendQueue.getInstance().addCommandToSend(command, 3000);
    }
}
