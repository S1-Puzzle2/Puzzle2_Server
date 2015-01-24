package at.fhv.puzzle2.server.game.state;

import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.server.game.Game;
import at.fhv.puzzle2.server.users.ClientManager;

public abstract class GameState {
    final Game _game;
    final ClientManager _clientManager;

    GameState(Game game, ClientManager clientManager) {
        _game = game;
        _clientManager = clientManager;
    }

    public abstract GameState processCommand(Command command);

    public GameState processDisconnectedClient(CommandConnection connection) {
        _clientManager.clientDisconnected(connection);

        return null;
    }

    public abstract boolean commandAllowedInGameState(Command command);

    boolean isClassOf(Object command, Class<?>... classes) {
        for(Class tmpClass : classes) {
            if(tmpClass.isInstance(command)) {
                return true;
            }
        }

        return false;
    }

    public abstract void enter(GameState lastState);
}
