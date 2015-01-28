package at.fhv.puzzle2.server.game;

import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.command.commands.error.NotAllowedCommand;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.logging.Logger;
import at.fhv.puzzle2.server.SendQueue;
import at.fhv.puzzle2.server.database.Database;
import at.fhv.puzzle2.server.entity.Puzzle;
import at.fhv.puzzle2.server.entity.manager.PuzzleEntityManager;
import at.fhv.puzzle2.server.game.state.BeforeGameStartState;
import at.fhv.puzzle2.server.game.state.GamePausedState;
import at.fhv.puzzle2.server.game.state.GameRunningState;
import at.fhv.puzzle2.server.game.state.GameState;
import at.fhv.puzzle2.server.users.ClientManager;

import java.io.IOException;
import java.sql.SQLException;

public class Game {
    private static final String TAG = "server.Game";

    private GameState _currentState;
    private final ClientManager _clientManager;
    private Puzzle _puzzle = null;

    public Game() {
        _clientManager = new ClientManager();
        _currentState = new BeforeGameStartState(this, _clientManager);

        //TODO currently a hacked version to retrieve the first puzzle
        /*PuzzleEntityManager puzzleEntityManager = new PuzzleEntityManager(Database.getInstance());
        try {
            setPuzzle(puzzleEntityManager.getPuzzleByID(puzzleEntityManager.getPuzzleList().get(0).getID()));
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }*/
    }

    public void processCommand(Command command) {
        if(!_currentState.commandAllowedInGameState(command)) {
            NotAllowedCommand notAllowedCommand = new NotAllowedCommand(command.getClientID());
            notAllowedCommand.setConnection(command.getConnection());
            //notAllowedCommand.setCommandType(command);

            SendQueue.getInstance().addCommandToSend(notAllowedCommand);

            return;
        }

        GameState state =_currentState.processCommand(command);
        if(state != null) {
            if(state instanceof GameRunningState) {
                Logger.getLogger().info(TAG, "Game started...");
            }

            GameState previousState = _currentState;

            _currentState = state;
            _currentState.enter(previousState);
        }
    }

    public void processDisconnectedConnection(CommandConnection connection) {
        GameState state = _currentState.processDisconnectedClient(connection);

        if(state != null) {
            GameState lastState = _currentState;

            if(state instanceof GamePausedState) {
                Logger.getLogger().info(TAG, "Game paused...");
            }
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
