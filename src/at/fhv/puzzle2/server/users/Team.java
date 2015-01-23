package at.fhv.puzzle2.server.users;

import at.fhv.puzzle2.communication.ClientID;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.server.users.client.Client;
import at.fhv.puzzle2.server.users.client.ClientType;
import at.fhv.puzzle2.server.users.client.state.NotConnectedClientState;
import at.fhv.puzzle2.server.entity.Puzzle;
import at.fhv.puzzle2.server.logic.manager.PuzzleManager;
import at.fhv.puzzle2.server.logic.manager.QuestionManager;
import at.fhv.puzzle2.server.users.client.state.NotReadyClientState;
import at.fhv.puzzle2.server.users.client.state.ReadyClientState;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Team {
    private String _teamName;
    private Client _unityClient = null;
    private Client _mobileClient = null;

    //We create a ID for the mobile client in advance
    //TODO use a random uuid, this is just for testing
    private final ClientID _mobileClientID = new ClientID("0dda1398-a293-11e4-89d3-123b93f75cba");

    //private final ClientID _mobileClientID = ClientID.createRandomClientID();

    private QuestionManager _questionManager;
    private PuzzleManager _puzzleManager;

    public Team(String name, QuestionManager questionManager) {
        _teamName = name;

        _questionManager = questionManager;
    }

    public void setPuzzle(Puzzle puzzle) {
        _puzzleManager = new PuzzleManager(puzzle);
    }

    public String getTeamName() {
        return _teamName;
    }

    /**
     * Registers a new client into the team
     * @param newClient Client
     * @return Returns true, if the client has been associated with the team
     */
    public boolean registerNewClient(Client newClient) {
        if(newClient.getClientType() == ClientType.Unity &&
                (_unityClient == null || !_unityClient.isConnected())) {
            _unityClient = newClient;
            _unityClient.swapClientState(new NotReadyClientState(_unityClient));

            return true;
        }

        return false;
    }

    /**
     * Registers a reconnected client
     * @param client Client
     * @return Returns true, if the user has been associated with the connection, false if the clientID is already
     * in use or it doesnt belong to this team
     */
    public boolean registerReconnectedClient(Client client) {
        switch (client.getClientType()) {
            case Unity:
                if(_unityClient == null || !_unityClient.isConnected()) {
                    _unityClient = client;
                    _unityClient.swapClientState(new NotReadyClientState(_unityClient));

                    return true;
                }

                break;
            case Mobile:
                if(Objects.equals(_mobileClientID, client.getClientID())) {
                    if(_mobileClient == null || !_mobileClient.isConnected()) {
                        _mobileClient = client;
                        _mobileClient.swapClientState(new NotReadyClientState(_mobileClient));

                        return true;
                    }
                }
        }

        return false;
    }

    public boolean isTeamReady() {
        return _mobileClient != null && _mobileClient.isReady() &&
                _unityClient != null && _unityClient.isReady();
    }

    public boolean belongsToTeam(ClientID clientID) {
        return (_mobileClient != null && Objects.equals(_mobileClient.getClientID(), clientID)) ||
                (_unityClient != null && Objects.equals(_unityClient.getClientID(), clientID));
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
        if(_mobileClient != null && _mobileClient.isConnected()) {
            if(Objects.equals(_mobileClient.getConnection(), connection)) {
                _mobileClient.swapClientState(new NotConnectedClientState(_mobileClient), true);
                _mobileClient.setConnection(null);
            } else {
                _mobileClient.swapClientState(new ReadyClientState(_mobileClient), true);
            }
        }

        if(_unityClient != null && _unityClient.isConnected()) {
            if(Objects.equals(_unityClient.getConnection(), connection)) {
                _unityClient.swapClientState(new NotConnectedClientState(_unityClient), true);
                _unityClient.setConnection(null);
            } else {
                //FIXME dont set it automatically to ReadyClient
                _unityClient.swapClientState(new ReadyClientState(_unityClient), true);
            }
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
}
