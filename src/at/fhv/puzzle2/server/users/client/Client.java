package at.fhv.puzzle2.server.users.client;

import at.fhv.puzzle2.communication.ClientID;
import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.logging.Logger;
import at.fhv.puzzle2.server.users.Team;
import at.fhv.puzzle2.server.users.client.state.ClientState;
import at.fhv.puzzle2.server.users.client.state.GameFinishedClientState;
import at.fhv.puzzle2.server.users.client.state.NotConnectedClientState;
import at.fhv.puzzle2.server.users.client.state.NotReadyClientState;

import java.util.Stack;

public abstract class Client {
    private static final String TAG = "server.Client";
    private CommandConnection _connection;
    private ClientID _clientID;

    Team _team;

    ClientState _currentState;
    private final Stack<ClientState> _stateStack;

    Client(CommandConnection connection, ClientID clientID) {
        _connection = connection;
        _clientID = clientID;

        _currentState = new NotConnectedClientState(this);
        _stateStack = new Stack<>();
    }

    public ClientState getCurrentState() {
        return _currentState;
    }

    public Client(CommandConnection connection) {
        this(connection, null);
    }

    public void setTeam(Team team) {
        _team = team;
    }

    public void setID(ClientID id) {
        _clientID = id;
    }

    public boolean isConnected() {
        return !(_currentState instanceof NotConnectedClientState);
    }

    public CommandConnection getConnection() {
        return _connection;
    }

    public void setConnection(CommandConnection connection) {
        _connection = connection;
    }

    public ClientID getClientID() {
        return _clientID;
    }

    public void gameFinished(boolean isWinning) {
        GameFinishedClientState gameFinishedClientState = new GameFinishedClientState(this);
        gameFinishedClientState.setIsWinning(isWinning);

        swapClientState(gameFinishedClientState);
    }

    public boolean isReady() {
        return !(_currentState instanceof NotConnectedClientState || _currentState instanceof NotReadyClientState);
    }

    public abstract void processCommand(Command command);

    public void swapClientState(ClientState state) {
        swapClientState(state, false);
    }

    public void swapClientState(ClientState state, boolean storeLastState) {
        if(storeLastState) {
            _stateStack.push(_currentState);
        }

        _currentState = state;
        _currentState.enter();
    }

    public void swapToLastState() {
        if(!_stateStack.empty()) {
             swapClientState(_stateStack.pop());
        } else {
            Logger.getLogger().error(TAG, "No last state is available");
        }
    }

    public void swapToDefaultState() {
        swapClientState(getStartingState());
    }

    public abstract ClientState fillDataInState(ClientState state);

    /**
     * This is the default state, which the client gets in when the game starts
     * Its SearchPart for the MobileClient
     * and SolvePuzzle for the UnityClient
     * @return ClientState
     */
    protected abstract ClientState getStartingState();
}
