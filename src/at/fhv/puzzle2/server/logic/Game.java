package at.fhv.puzzle2.server.logic;

import at.fhv.puzzle2.communication.application.command.AbstractCommand;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.server.DisconnectedConnectionsQueue;
import at.fhv.puzzle2.server.ReceivedCommandQueue;
import at.fhv.puzzle2.server.client.ClientManager;
import at.fhv.puzzle2.server.state.BeforeGameStartState;
import at.fhv.puzzle2.server.state.GameState;

public class Game {
    GameState _currentState;
    private ClientManager _clientManager;

    public Game(ClientManager clientManager) {
        _currentState = new BeforeGameStartState(clientManager, this);
    }

    public void setGameState(GameState state) {
        _currentState = state;
    }

    public GameState getGameState() {
        return _currentState;
    }

    public void processCommand(AbstractCommand command) {
        _currentState.processCommand(command);
    }

    public void processDisconnectedConnection(CommandConnection connection) {
        _currentState.processDisconnectedClient(connection);
    }

    public boolean commandAllowed(AbstractCommand command) {
        return _currentState.commandAllowedInGameState(command);
    }

    public void timeElapsed(int time) {

    }
}