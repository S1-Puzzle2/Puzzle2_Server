package at.fhv.puzzle2.server.users.client.state;

import at.fhv.puzzle2.communication.application.command.commands.mobile.PuzzleFinishedCommand;
import at.fhv.puzzle2.server.SendQueue;
import at.fhv.puzzle2.server.users.client.Client;

public class PuzzleFinishedClientState extends ClientState {
    public PuzzleFinishedClientState(Client client) {
        super(client);
    }

    @Override
    public void enter() {
        PuzzleFinishedCommand puzzleFinishedCommand = new PuzzleFinishedCommand(_client.getClientID());
        puzzleFinishedCommand.setConnection(_client.getConnection());

        SendQueue.getInstance().addCommandToSend(puzzleFinishedCommand, 3000);
    }
}
