package at.fhv.puzzle2.server.game.state;

import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.command.commands.mobile.BarcodeScannedCommand;
import at.fhv.puzzle2.communication.application.command.commands.mobile.QuestionAnsweredCommand;
import at.fhv.puzzle2.communication.application.command.commands.unity.PuzzleFinishedCommand;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.server.users.client.Client;
import at.fhv.puzzle2.server.users.ClientManager;
import at.fhv.puzzle2.server.game.Game;

public class GameRunningState extends GameState {

    public GameRunningState(Game game, ClientManager clientManager) {
        super(game, clientManager);
    }

    @Override
    public void processCommand(Command command) {
        Client client = _clientManager.getClientByID(command.getClientID());
    }

    @Override
    public void processDisconnectedClient(CommandConnection connection) {
        _clientManager.clientDisconnected(connection);

        if(_clientManager.areAllReady()) {

        }
    }

    @Override
    public boolean commandAllowedInGameState(Command command) {
        return isClassOf(command.getClass(),
                BarcodeScannedCommand.class, QuestionAnsweredCommand.class,
                PuzzleFinishedCommand.class);
    }
}
