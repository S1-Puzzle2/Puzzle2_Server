package at.fhv.puzzle2.server.game.state;

import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.command.commands.configurator.ResetGameCommand;
import at.fhv.puzzle2.server.game.Game;
import at.fhv.puzzle2.server.users.ClientManager;
import at.fhv.puzzle2.server.users.Team;
import at.fhv.puzzle2.server.users.client.Client;

import java.util.Optional;

public class GameFinishedState extends GameState {
    private final Team _winningTeam;

    GameFinishedState(Game game, ClientManager clientManager, Team winningTeam) {
        super(game, clientManager);

        _winningTeam = winningTeam;
    }

    @Override
    public Optional<GameState> processCommand(Command command) {
        if(command instanceof ResetGameCommand) {
            //TODO return beforeGameState
        }

        return Optional.empty();
    }

    @Override
    public boolean commandAllowedInGameState(Command command) {
        return isClassOf(command, ResetGameCommand.class);
    }

    @Override
    public void enter(GameState lastState) {
        for(Client client : _clientManager.getConnectedClients()) {
            client.gameFinished();
        }
    }
}
