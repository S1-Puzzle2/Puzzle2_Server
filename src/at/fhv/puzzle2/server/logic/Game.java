package at.fhv.puzzle2.server.logic;

import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.server.client.ClientManager;
import at.fhv.puzzle2.server.entity.Puzzle;
import at.fhv.puzzle2.server.state.BeforeGameStartState;
import at.fhv.puzzle2.server.state.GameState;

public class Game {
    GameState _currentState;
    private ClientManager _clientManager;
    private Puzzle _puzzle = null;

    public Game(ClientManager clientManager) {
        _currentState = new BeforeGameStartState(this, clientManager);
    }

    public void setGameState(GameState state) {
        _currentState = state;
    }

    public GameState getGameState() {
        return _currentState;
    }

    public void processCommand(Command command) {
        _currentState.processCommand(command);
    }

    public void processDisconnectedConnection(CommandConnection connection) {
        _currentState.processDisconnectedClient(connection);
    }

    public boolean commandAllowed(Command command) {
        return _currentState.commandAllowedInGameState(command);
    }

    public Puzzle getPuzzle() {
        return _puzzle;
    }

    public void setPuzzle(Puzzle puzzle) {
        _puzzle = puzzle;
    }
}
