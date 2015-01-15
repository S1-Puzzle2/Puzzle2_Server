package at.fhv.puzzle2.server.client;

import at.fhv.puzzle2.communication.application.connection.CommandConnection;

import java.util.Objects;

public class Team {
    private String _teamName;
    private Client _unityClient;
    private Client _mobileClient;

    public Team(String name) {
        _teamName = name;
    }

    public String getTeamName() {
        return _teamName;
    }

    public void setUnityClient(Client unityClient) {
        _unityClient = unityClient;
    }

    public Client getUnityClient() {
        return _unityClient;
    }

    public void setMobileClient(Client mobileClient) {
        _mobileClient = mobileClient;
    }

    public Client getMobileClient() {
        return _mobileClient;
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
