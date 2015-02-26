package at.fhv.puzzle2.server.game.state;

import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.command.commands.GameStartCommand;
import at.fhv.puzzle2.communication.application.command.commands.RegisterGameStatusListenerCommand;
import at.fhv.puzzle2.communication.application.command.commands.mobile.AllPartsUnlockedCommand;
import at.fhv.puzzle2.communication.application.command.commands.mobile.PartScannedCommand;
import at.fhv.puzzle2.communication.application.command.commands.mobile.QuestionAnsweredCommand;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.server.SendQueue;
import at.fhv.puzzle2.server.game.Game;
import at.fhv.puzzle2.server.users.ClientManager;
import at.fhv.puzzle2.server.users.Team;
import at.fhv.puzzle2.server.users.client.Client;

import java.util.List;
import java.util.Optional;

public class GameRunningState extends GameState {

    public GameRunningState(Game game, ClientManager clientManager) {
        super(game, clientManager);
    }

    @Override
    public Optional<GameState> processCommand(Command command) {
        Client client = _clientManager.getClientByID(command.getClientID());

        if(command instanceof AllPartsUnlockedCommand) {
            Team team = _clientManager.getTeamOfClient(client);
            if (team.isMobileFinished()) {
                _clientManager.gameFinished(team);
            }
        }else if(command instanceof RegisterGameStatusListenerCommand) {
            if(client != null) {
                _game.addStatusChangedListener(client);
            }
        } else {
            client.processCommand(command);
        }

        return Optional.empty();
    }

    @Override
    public GameState processDisconnectedClient(CommandConnection connection) {
        super.processDisconnectedClient(connection);

        if(!_clientManager.areAllReady()) {
            return new GamePausedState(_game, _clientManager);
        }

        return null;
    }

    @Override
    public boolean commandAllowedInGameState(Command command) {
        return isClassOf(command,
                PartScannedCommand.class, QuestionAnsweredCommand.class,
                RegisterGameStatusListenerCommand.class,
                AllPartsUnlockedCommand.class);
    }

    @Override
    public void enter(GameState lastState) {
        List<Client> clientList = _clientManager.getConnectedClients();

        for(Client client: clientList) {
            GameStartCommand gameStartCommand = new GameStartCommand(client.getClientID());
            gameStartCommand.setConnection(client.getConnection());
            gameStartCommand.setTime(_game.getTimeElapsed());

            SendQueue.getInstance().addCommandToSend(gameStartCommand);

            if(lastState instanceof BeforeGameStartState) {
                client.gameStarted();
            } else if(lastState instanceof GamePausedState) {
                client.gameResume();
            }
        }
    }
}
