package at.fhv.puzzle2.server.users.client.state;

import at.fhv.puzzle2.communication.application.command.commands.GameFinishedCommand;
import at.fhv.puzzle2.server.SendQueue;
import at.fhv.puzzle2.server.users.Team;
import at.fhv.puzzle2.server.users.client.Client;

public class GameFinishedClientState extends ClientState {
    private Team _winningTeam;

    public GameFinishedClientState(Client client) {
        super(client);
    }

    @Override
    public void enter() {
        GameFinishedCommand gameFinishedCommand = new GameFinishedCommand(_client.getClientID());
        gameFinishedCommand.setConnection(_client.getConnection());
        gameFinishedCommand.setIsWinning(_client.getTeam().isWinning());

        SendQueue.getInstance().addCommandToSend(gameFinishedCommand);
    }

    @Override
    public String toString() {
        return "GameFinished";
    }
}
