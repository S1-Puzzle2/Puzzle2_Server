package at.fhv.puzzle2.server.users.client.state;

import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.command.commands.GameFinishedCommand;
import at.fhv.puzzle2.communication.application.command.commands.mobile.PuzzleFinishedCommand;
import at.fhv.puzzle2.server.SendQueue;
import at.fhv.puzzle2.server.users.client.Client;

public class PuzzleFinishedClientState extends ClientState {
    public PuzzleFinishedClientState(Client client) {
        super(client);
    }

    @Override
    public ClientState handleCommand(Command command) {
        return null;
    }

    @Override
    public void enter() {
        PuzzleFinishedCommand puzzleFinishedCommand = new PuzzleFinishedCommand(_client.getClientID());
        puzzleFinishedCommand.setConnection(_client.getConnection());

        GameFinishedCommand gameFinishedCommand = new GameFinishedCommand(_client.getClientID());
        gameFinishedCommand.setConnection(_client.getConnection());
        gameFinishedCommand.setIsWinning(true);

        SendQueue.getInstance().addCommandToSend(puzzleFinishedCommand, 3000);
        SendQueue.getInstance().addCommandToSend(gameFinishedCommand, 10000);
    }
}
