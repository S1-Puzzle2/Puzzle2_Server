package at.fhv.puzzle2.server.users.client;

import at.fhv.puzzle2.communication.ClientID;
import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.logging.Logger;
import at.fhv.puzzle2.server.users.Team;
import at.fhv.puzzle2.server.users.client.state.*;

import java.util.Stack;

public abstract class Client {
    private static final String TAG = "server.Client";
    private CommandConnection _connection;
    private ClientID _clientID;

    Team _team;

    ClientState _currentState;
    private Stack<ClientState> _stateStack;

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

    public Team getTeam() {
        return _team;
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

    public void gameFinished() {
        GameFinishedClientState gameFinishedClientState = new GameFinishedClientState(this);

        enterClientState(gameFinishedClientState);
    }

    public void gameStarted() {

        enterClientState(getStartingState());
    }

    public void gameResume() {
        if(!_stateStack.empty()) {
            enterClientState(_stateStack.pop());
        } else {
            Logger.getLogger().error(TAG, "There is no clientstate on the stack :O");
        }
    }

    public void gamePaused() {
        _stateStack.push(_currentState);

        enterClientState(new ReadyClientState(this));
    }

    public void connected(Client oldClient) {
        //Copy the state stack
        oldClient._stateStack.forEach(_stateStack::add);

        //Set the team
        _team = oldClient.getTeam();

        enterClientState(new NotReadyClientState(this));
    }

    public void disconnected() {
        _connection = null;

        _stateStack.push(_currentState);
        enterClientState(new NotConnectedClientState(this));
    }

    public boolean isReady() {
        return !(_currentState instanceof NotConnectedClientState || _currentState instanceof NotReadyClientState);
    }

    public void processCommand(Command command) {
        _currentState.handleCommand(command)
                .ifPresent(this::enterClientState); //If we receive a new ClientState, then enter it
    }

    protected void enterClientState(ClientState state) {
        _currentState = state;

        _currentState.enter();

        _team.statusChanged();
    }

    /**
     * This is the default state, which the client gets in when the game starts
     * Its SearchPart for the MobileClient
     * and SolvePuzzle for the UnityClient
     * @return ClientState
     */
    protected abstract ClientState getStartingState();
}
