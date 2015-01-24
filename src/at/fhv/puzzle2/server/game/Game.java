package at.fhv.puzzle2.server.game;

import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.command.commands.error.NotAllowedCommand;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.logging.Logger;
import at.fhv.puzzle2.server.SendQueue;
import at.fhv.puzzle2.server.entity.Puzzle;
import at.fhv.puzzle2.server.game.state.BeforeGameStartState;
import at.fhv.puzzle2.server.game.state.GamePausedState;
import at.fhv.puzzle2.server.game.state.GameRunningState;
import at.fhv.puzzle2.server.game.state.GameState;
import at.fhv.puzzle2.server.users.ClientManager;

public class Game {
    private static final String TAG = "server.Game";

    private GameState _currentState;
    private final ClientManager _clientManager;
    private Puzzle _puzzle = null;

    public Game() {
        _clientManager = new ClientManager();
        _currentState = new BeforeGameStartState(this, _clientManager);
    }

    public void processCommand(Command command) {
        if(_currentState.commandAllowedInGameState(command)) {
            NotAllowedCommand notAllowedCommand = new NotAllowedCommand(command.getClientID());
            SendQueue.getInstance().addCommandToSend(notAllowedCommand);

            return;
        }

        GameState state =_currentState.processCommand(command);
        if(state != null) {
            if(state instanceof GameRunningState) {
                Logger.getLogger().info(TAG, "Game started...");
            } else if(state instanceof GamePausedState) {
                Logger.getLogger().info(TAG, "Game paused...");
            }

            _currentState = state;
        }
    }

    public void processDisconnectedConnection(CommandConnection connection) {
        GameState state = _currentState.processDisconnectedClient(connection);

        if(state != null) {
            GameState lastState = _currentState;
            _currentState = state;

            _currentState.enter(lastState);
        }
    }

    public boolean commandAllowed(Command command) {
        return _currentState.commandAllowedInGameState(command);
    }

    public Puzzle getPuzzle() {
        return _puzzle;
    }

    public void setPuzzle(Puzzle puzzle) {
        _puzzle = puzzle;
        _clientManager.setPuzzle(puzzle);
    }
}
