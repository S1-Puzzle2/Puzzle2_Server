package at.fhv.puzzle2.server.team;

import at.fhv.puzzle2.communication.ClientID;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.server.client.Client;
import at.fhv.puzzle2.server.client.ClientType;
import at.fhv.puzzle2.server.client.MobileClient;
import at.fhv.puzzle2.server.client.UnityClient;
import at.fhv.puzzle2.server.client.state.NotConnectedClientState;
import at.fhv.puzzle2.server.entity.Puzzle;
import at.fhv.puzzle2.server.logic.manager.PuzzleManager;
import at.fhv.puzzle2.server.logic.manager.QuestionManager;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Team {
    private String _teamName;
    private UnityClient _unityClient;
    private MobileClient _mobileClient;

    private QuestionManager _questionManager;
    private PuzzleManager _puzzleManager;

    public Team(String name) {
        _teamName = name;



        _unityClient = null;
        _mobileClient = new MobileClient(null, ClientID.createRandomClientID());
    }

    public String getTeamName() {
        return _teamName;
    }

    /**
     * Registers a new client into the team
     * @param type Type of the client
     * @param connection The Connection of the client
     * @return Returns the ClientID, or null, if he isnt allowed in this team
     */
    public ClientID registerNewClient(ClientType type, CommandConnection connection) {
        switch (type) {
            case Unity:
                if(_unityClient == null) {
                    ClientID newID = ClientID.createRandomClientID();
                    _unityClient = new UnityClient(connection, newID);

                    return newID;
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
    public boolean registerReconnectedClient(ClientType type, CommandConnection connection, ClientID clientID) {
        switch (type) {
            case Unity:
                if(_unityClient != null && !_unityClient.isConnected()) {
                    _unityClient.setConnection(connection);
                    return true;
                }
                return false;
            case Mobile:
                _mobileClient = new MobileClient(connection, clientID);
                /*if(_mobileClient != null && !_mobileClient.isConnected()) {
                    _mobileClient.setConnection(connection);
                }*/
                return true;
            default:
                return false;
        }
    }

    public boolean isTeamReady() {
        return _mobileClient != null && _mobileClient.isReady() &&
                _unityClient != null && _unityClient.isReady();
    }

    public boolean belongsToTeam(ClientID clientID) {
        return (_mobileClient != null && Objects.equals(_mobileClient.getClientID(), clientID)) ||
                (_unityClient != null && Objects.equals(_unityClient.getClientID(), clientID));
    }

    public boolean belongsToTeam(CommandConnection connection) {
        return _mobileClient.getConnection().equals(connection) || _unityClient.getConnection().equals(connection);
    }

    public Client getClientByID(ClientID clientID) {
        if(Objects.equals(_mobileClient.getClientID(), clientID)) {
            return _mobileClient;
        }

        if(Objects.equals(_unityClient.getClientID(), clientID)) {
            return _unityClient;
        }

        return null;
    }

    public void clientDisconnected(CommandConnection connection) {
        if(_mobileClient != null && Objects.equals(_mobileClient.getConnection(), connection)) {
            _mobileClient.swapClientState(new NotConnectedClientState(_mobileClient), true);
        }

        if(_unityClient != null && Objects.equals(_unityClient.getConnection(), connection)) {
            _unityClient.setConnection(null);
            _unityClient.swapClientState(new NotConnectedClientState(_unityClient), true);
        }
    }

    public List<CommandConnection> getConnections() {
        List<CommandConnection> connectionList = new LinkedList<>();
        if(_unityClient.isConnected()) {
            connectionList.add(_unityClient.getConnection());
        }

        if(_mobileClient.isConnected()) {
            connectionList.add(_mobileClient.getConnection());
        }

        return connectionList;
    }

    public void setPuzzle(Puzzle puzzle) {
        _puzzleManager.setPuzzle(puzzle);
    }

    public boolean puzzlePartsToFindAvailable() {
        return _puzzleManager.partsAvailable();
    }
}
