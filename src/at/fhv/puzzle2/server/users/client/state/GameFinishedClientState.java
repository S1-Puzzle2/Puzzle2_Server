package at.fhv.puzzle2.server.users.client.state;

import at.fhv.puzzle2.communication.application.command.commands.GameFinishedCommand;
import at.fhv.puzzle2.server.SendQueue;
import at.fhv.puzzle2.server.users.client.Client;

public class GameFinishedClientState extends ClientState {
    private boolean _isWinning;

    public GameFinishedClientState(Client client) {
        super(client);
    }

    public void setIsWinning(boolean isWinning) {
        _isWinning = isWinning;
    }

    @Override
    public void enter() {
        GameFinishedCommand gameFinishedCommand = new GameFinishedCommand(_client.getClientID());
        gameFinishedCommand.setConnection(_client.getConnection());
        gameFinishedCommand.setIsWinning(_isWinning);

        SendQueue.getInstance().addCommandToSend(gameFinishedCommand);
    }
}
