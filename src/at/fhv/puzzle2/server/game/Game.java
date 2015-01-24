package at.fhv.puzzle2.server.game;

import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.command.commands.PauseCommand;
import at.fhv.puzzle2.communication.application.command.commands.error.NotAllowedCommand;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.server.SendQueue;
import at.fhv.puzzle2.server.entity.Puzzle;
import at.fhv.puzzle2.server.game.state.BeforeGameStartState;
import at.fhv.puzzle2.server.game.state.GameRunningState;
import at.fhv.puzzle2.server.game.state.GameState;
import at.fhv.puzzle2.server.users.ClientManager;
import at.fhv.puzzle2.server.users.client.Client;
import at.fhv.puzzle2.server.users.client.state.ReadyClientState;

import java.util.List;

public class Game {
    private GameState _currentState;
    private final ClientManager _clientManager;
    private Puzzle _puzzle = null;

    public Game() {
        _clientManager = new ClientManager();
        _currentState = new BeforeGameStartState(this, _clientManager);
    }

    public void setGameState(GameState state) {
        _currentState = state;
        _currentState.enter();
    }

    public GameState getGameState() {
        return _currentState;
    }

    public void processCommand(Command command) {
        if(_currentState.commandAllowedInGameState(command)) {
            NotAllowedCommand notAllowedCommand = new NotAllowedCommand(command.getClientID());
            SendQueue.getInstance().addCommandToSend(notAllowedCommand);

            return;
        }

        _currentState.processCommand(command);
    }

    public void processDisconnectedConnection(CommandConnection connection) {
        GameState state = _currentState.processDisconnectedClient(connection);

        if(_currentState instanceof GameRunningState && state instanceof BeforeGameStartState) {
            //Alright, so one of the clients disconnected. Now notify all other clients that the game
            //has paused and set their status to Ready
            List<Client> clientList = _clientManager.getAllClients();

            for(Client client: clientList) {
                client.swapClientState(new ReadyClientState(client), true);
                PauseCommand pauseCommand = new PauseCommand(client.getClientID());
                pauseCommand.setConnection(client.getConnection());

                SendQueue.getInstance().addCommandToSend(pauseCommand);
            }
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
