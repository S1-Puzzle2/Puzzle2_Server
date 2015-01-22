package at.fhv.puzzle2.server.client;

import at.fhv.puzzle2.communication.ClientID;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.server.client.state.ClientState;
import at.fhv.puzzle2.server.client.state.NotConnectedCientState;

import java.util.Stack;

public class Client {
    private CommandConnection _connection;
    private ClientID _clientID;

    private ClientState _currentState;

    private Stack<ClientState> _stateStack;


    public Client(CommandConnection connection, ClientID clientID) {
        _connection = connection;
        _clientID = clientID;

        _currentState = new NotConnectedCientState(this);
        _stateStack = new Stack<>();
    }

    public boolean isConnected() {
        return _connection != null;
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

    public ClientState getState() {
        return _currentState;
    }

    public void swapClientState(ClientState state) {
        swapClientState(state, false);
    }

    public void swapToLastState() {
        _currentState = _stateStack.pop();
    }
}
