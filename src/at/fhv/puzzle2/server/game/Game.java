package at.fhv.puzzle2.server.game;

import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.command.commands.GameInfoCommand;
import at.fhv.puzzle2.communication.application.command.commands.GameStatusChangedCommand;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.logging.Logger;
import at.fhv.puzzle2.server.SendQueue;
import at.fhv.puzzle2.server.StatusChangedListener;
import at.fhv.puzzle2.server.database.Database;
import at.fhv.puzzle2.server.dto_factory.PuzzleDTOFactory;
import at.fhv.puzzle2.server.entity.Puzzle;
import at.fhv.puzzle2.server.entity.manager.PuzzleEntityManager;
import at.fhv.puzzle2.server.game.state.BeforeGameStartState;
import at.fhv.puzzle2.server.game.state.GamePausedState;
import at.fhv.puzzle2.server.game.state.GameRunningState;
import at.fhv.puzzle2.server.game.state.GameState;
import at.fhv.puzzle2.server.users.ClientManager;
import at.fhv.puzzle2.server.users.client.Client;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Game implements StatusChangedListener {
    private static final String TAG = "server.Game";

    private GameState _currentState;
    private final ClientManager _clientManager;
    private Puzzle _puzzle = null;

    private long _timeElapsed = 0;

    private boolean _statusChanged = false;

    private List<Client> _statusChangedListeners = new LinkedList<>();

    public Game() {
        _clientManager = new ClientManager();
        _currentState = new BeforeGameStartState(this, _clientManager);

        _clientManager.registerStatusChangedListener(this);

        //TODO just for testing
        PuzzleEntityManager pem = new PuzzleEntityManager(Database.getInstance());
        setPuzzle(pem.getPuzzleByID(2).get());
        setPuzzle(new Puzzle(2, "hammergood puzzle"));
    }

    public void addStatusChangedListener(Client client) {
        _statusChangedListeners.add(client);
    }

    public void processCommand(Command command) {
        if(!_currentState.commandAllowedInGameState(command)) {
            //Just ignore those packets
            /*NotAllowedCommand notAllowedCommand = new NotAllowedCommand(command.getClientID());
            notAllowedCommand.setConnection(command.getConnection());
            notAllowedCommand.setCommandType(command.getCommandType());

            SendQueue.getInstance().addCommandToSend(notAllowedCommand);*/

            return;
        }

        Optional<GameState> stateOptional =_currentState.processCommand(command);
        if(stateOptional.isPresent()) {
            if(stateOptional.get() instanceof GameRunningState) {
                Logger.getLogger().info(TAG, "Game started...");
            }

            GameState previousState = _currentState;

            _currentState = stateOptional.get();
            _currentState.enter(previousState);
        }
    }

    public void timeElapsed(long time) {
        if(_currentState instanceof GameRunningState) {
            //Only advance the time, when the game is really running
            _timeElapsed += time;
        }
    }

    public long getTimeElapsed() {
        return _timeElapsed;
    }

    public void processDisconnectedConnection(CommandConnection connection) {
        //Remove any status changed listeners, which are disconnected
        Iterator<Client> clientIterator = _statusChangedListeners.iterator();
        while(clientIterator.hasNext()) {
            Client client = clientIterator.next();

            if(client.getConnection().equals(connection)) {
                clientIterator.remove();

                break;
            }
        }

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

    public Puzzle getPuzzle() {
        return _puzzle;
    }

    public void setPuzzle(Puzzle puzzle) {
        _puzzle = puzzle;

        _clientManager.setPuzzle(puzzle);

        for(Client client: _clientManager.getConnectedClients()) {
            GameInfoCommand gameInfoCommand = new GameInfoCommand(client.getClientID());
            gameInfoCommand.setConnection(client.getConnection());
            gameInfoCommand.setPuzzle(PuzzleDTOFactory.createPuzzleDTO(puzzle));

            SendQueue.getInstance().addCommandToSend(gameInfoCommand);
        }
    }



    public void unsetStatusChanged() {
        _statusChanged = false;
    }

    public boolean hasStatusChanged() {
        return _statusChanged;
    }

    @Override
    public void statusChanged() {
        _statusChanged = true;
    }

    public void notifyStatusChangedListeners() {
        for(Client client : _statusChangedListeners) {
            GameStatusChangedCommand command = new GameStatusChangedCommand(client.getClientID());
            command.setConnection(client.getConnection());

            command.setFirstTeamStatus(_clientManager.getFirstTeam().getStatus());
            command.setSecondTeamStatus(_clientManager.getSecondTeam().getStatus());

            SendQueue.getInstance().addCommandToSend(command);
        }
    }
}
