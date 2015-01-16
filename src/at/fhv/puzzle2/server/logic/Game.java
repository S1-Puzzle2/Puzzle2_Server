package at.fhv.puzzle2.server.logic;

import at.fhv.puzzle2.server.client.ClientManager;
import at.fhv.puzzle2.server.state.BeforeGameStartState;
import at.fhv.puzzle2.server.state.GameState;

public class Game {
    GameState _currentState;
    private ClientManager _clientManager;

    public Game(ClientManager clientManager) {
        _currentState = new BeforeGameStartState(clientManager);
    }

    public void setGameState(GameState state) {
        _currentState = state;
    }

    public GameState getGameState() {
        return _currentState;
    }
}
