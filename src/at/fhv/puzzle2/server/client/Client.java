package at.fhv.puzzle2.server.client;

import at.fhv.puzzle2.communication.ClientID;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;

public class Client {
    private CommandConnection _connection;
    private ClientID _clientID;
    private ClientType _clientType;

    private boolean _isReady = false;

    public Client(ClientType type, CommandConnection connection, ClientID clientID) {
        _clientType = type;
        _connection = connection;
        _clientID = clientID;
    }

    public boolean isReady() {
        return _isReady;
    }

    public boolean isConnected() {
        return _connection != null;
    }

    public void setIsReady(boolean isReady) {
        this._isReady = isReady;
    }

    public CommandConnection getConnection() {
        return _connection;
    }

    public void setConnection(CommandConnection connection) {
        _connection = connection;
    }

    public ClientType getClientType() {
        return _clientType;
    }

    public ClientID getClientID() {
        return _clientID;
    }

}
