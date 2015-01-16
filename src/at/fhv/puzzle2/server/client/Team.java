package at.fhv.puzzle2.server.client;

import at.fhv.puzzle2.communication.application.connection.CommandConnection;

import java.util.Objects;
import java.util.UUID;

public class Team {
    private String _teamName;
    private Client _unityClient;
    private Client _mobileClient;

    public Team(String name) {
        _teamName = name;

        _unityClient = null;
        _mobileClient = new Client(ClientType.Mobile, null, UUID.randomUUID().toString());
    }

    public String getTeamName() {
        return _teamName;
    }

    /**
     * Registers a new client into the team
     * @param type Type of the client
     * @param connection The Connection of the client
     * @return Returns the UUID of the client, or null, if he isnt allowed in this team
     */
    public String registerNewClient(ClientType type, CommandConnection connection) {
        switch (type) {
            case Unity:
                if(_unityClient == null) {
                    String randomUUID = UUID.randomUUID().toString();
                    _unityClient = new Client(type, connection, randomUUID);
                    return randomUUID;
                }
            case Mobile:
            default:
                return null;
        }
    }

    /**
     * Registers a reconnected client
     * @param type
     * @param connection
     * @param clientID
     * @return Returns true, if the user has been associated with the connection, false if the clientID is already
     * in use or it doesnt belong to this team
     */
    public boolean registerReconnectedClient(ClientType type, CommandConnection connection, String clientID) {
        switch (type) {
            case Unity:
                if(_unityClient != null && !_unityClient.isConnected()) {
                    _unityClient.setConnection(connection);
                    return true;
                }
                return false;
            case Mobile:
                if(_mobileClient != null && !_mobileClient.isConnected()) {
                    _mobileClient.setConnection(connection);
                }
            default:
                return false;
        }
    }

    public boolean isTeamReady() {
        return _mobileClient.isReady() && _unityClient.isReady();
    }

    public boolean belongsToTeam(String clientID) {
        return Objects.equals(_mobileClient.getClientID(), clientID) || Objects.equals(_unityClient.getClientID(), clientID);
    }

    public boolean belongsToTeam(CommandConnection connection) {
        return _mobileClient.getConnection().equals(connection) || _unityClient.getConnection().equals(connection);
    }

    public Client getClientByID(String clientID) {
        if(Objects.equals(_mobileClient.getClientID(), clientID)) {
            return _mobileClient;
        }

        if(Objects.equals(_unityClient.getClientID(), clientID)) {
            return _unityClient;
        }

        return null;
    }

    public void clientDisconnected(CommandConnection connection) {
        if(_mobileClient.getConnection().equals(connection)) {
            _mobileClient.setConnection(null);
            _mobileClient.setIsReady(false);
        }

        if(_unityClient.getConnection().equals(connection)) {
            _unityClient.setConnection(null);
            _unityClient.setIsReady(false);
        }
    }
}
