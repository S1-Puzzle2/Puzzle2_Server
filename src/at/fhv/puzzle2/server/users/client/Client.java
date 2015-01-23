package at.fhv.puzzle2.server.users.client;

import at.fhv.puzzle2.communication.ClientID;
import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.server.users.Team;
import at.fhv.puzzle2.server.users.client.state.ClientState;
import at.fhv.puzzle2.server.users.client.state.NotConnectedClientState;
import at.fhv.puzzle2.server.users.client.state.NotReadyClientState;

import java.util.Stack;

public class Client {
    private CommandConnection _connection;
    private ClientID _clientID;

    private ClientType _type;

    private Team _team;

    protected ClientState _currentState;
    protected Stack<ClientState> _stateStack;

    public Client(ClientType type, CommandConnection connection, ClientID clientID) {
        _connection = connection;
        _clientID = clientID;
        _type = type;

        _currentState = new NotConnectedClientState(this);
        _stateStack = new Stack<>();
    }

    public Client(ClientType type, CommandConnection connection) {
        this(type, connection, null);
    }

    public void setTeam(Team team) {
        _team = team;
    }

    public void setID(ClientID id) {
        _clientID = id;
    }

    public ClientType getClientType() {
        return _type;
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

    public void swapClientState(ClientState state, boolean storeLastState) {
        if(storeLastState) {
            _stateStack.push(_currentState);
        }

        _currentState = state;
    }

    public boolean isReady() {
        return !(_currentState instanceof NotConnectedClientState || _currentState instanceof NotReadyClientState);
    }

    public void processCommand(Command command) {
        ClientState state = _currentState.handleCommand(command);

        if(state != null) {

        }
    }

    public void swapClientState(ClientState state) {
        swapClientState(state, false);
    }

    public void swapToLastState() {
        _currentState = _stateStack.pop();
    }
}
