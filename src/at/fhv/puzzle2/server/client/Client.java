package at.fhv.puzzle2.server.client;

import at.fhv.puzzle2.communication.ClientID;
import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.server.client.state.ClientState;
import at.fhv.puzzle2.server.client.state.NotConnectedClientState;
import at.fhv.puzzle2.server.client.state.NotReadyClientState;

import java.util.Stack;

public abstract class Client {
    protected CommandConnection _connection;
    protected ClientID _clientID;

    protected ClientState _currentState;

    protected Stack<ClientState> _stateStack;


    public Client(CommandConnection connection, ClientID clientID) {
        _connection = connection;
        _clientID = clientID;

        _currentState = new NotConnectedClientState(this);
        _stateStack = new Stack<>();
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
    }

    public void swapClientState(ClientState state) {
        swapClientState(state, false);
    }

    public void swapToLastState() {
        _currentState = _stateStack.pop();
    }
}
