package at.fhv.puzzle2.server.game.state;

import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.command.commands.GameStartCommand;
import at.fhv.puzzle2.communication.application.command.commands.mobile.BarcodeScannedCommand;
import at.fhv.puzzle2.communication.application.command.commands.mobile.PuzzleFinishedCommand;
import at.fhv.puzzle2.communication.application.command.commands.mobile.QuestionAnsweredCommand;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.server.SendQueue;
import at.fhv.puzzle2.server.game.Game;
import at.fhv.puzzle2.server.users.ClientManager;
import at.fhv.puzzle2.server.users.client.Client;

import java.util.List;

public class GameRunningState extends GameState {

    public GameRunningState(Game game, ClientManager clientManager) {
        super(game, clientManager);
    }

    @Override
    public void processCommand(Command command) {
        Client client = _clientManager.getClientByID(command.getClientID());
        client.processCommand(command);
    }

    @Override
    public GameState processDisconnectedClient(CommandConnection connection) {
        super.processDisconnectedClient(connection);

        if(!_clientManager.areAllReady()) {
            return new BeforeGameStartState(_game, _clientManager);
        }

        return null;
    }

    @Override
    public boolean commandAllowedInGameState(Command command) {
        return isClassOf(command.getClass(),
                BarcodeScannedCommand.class, QuestionAnsweredCommand.class,
                PuzzleFinishedCommand.class);
    }

    @Override
    public void enter() {
        List<Client> clientList = _clientManager.getAllClients();

        for(Client client: clientList) {
            GameStartCommand gameStartCommand = new GameStartCommand(client.getClientID());
            gameStartCommand.setConnection(client.getConnection());
            gameStartCommand.setTime(0);

            client.swapToLastStateOrDefault();

            SendQueue.getInstance().addCommandToSend(gameStartCommand);
        }
    }
}
