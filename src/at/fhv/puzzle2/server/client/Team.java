package at.fhv.puzzle2.server.client;

import at.fhv.puzzle2.communication.ClientID;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.server.entity.Puzzle;
import at.fhv.puzzle2.server.entity.Question;
import at.fhv.puzzle2.server.logic.manager.PuzzleManager;
import at.fhv.puzzle2.server.logic.manager.QuestionManager;

import java.util.List;
import java.util.Objects;

public class Team {
    private String _teamName;
    private Client _unityClient;
    private Client _mobileClient;

    private QuestionManager _questionManager;
    private PuzzleManager _puzzleManager;

    public Team(String name/*, QuestionManager questionManager, PuzzleManager puzzleManager*/) {
        _teamName = name;

        _unityClient = null;
        _mobileClient = new Client(ClientType.Mobile, null, ClientID.createRandomClientID());

        /*_questionManager = questionManager;
        _puzzleManager = puzzleManager;*/
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
                    _unityClient = new Client(type, connection, newID);

                    return newID;
                } else if(!_unityClient.isConnected()) {
                    //If unity disconnected, we just asign the new connection
                    ClientID clientID = _unityClient.getClientID();
                    _unityClient.setConnection(connection);

                    return clientID;
                }
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
                if(_unityClient != null && !_unityClient.isConnected() && Objects.equals(_unityClient.getClientID(), clientID)) {
                    _unityClient.setConnection(connection);
                    return true;
                }
                return false;
            case Mobile:
                if(_mobileClient != null && !_mobileClient.isConnected() && Objects.equals(_mobileClient.getClientID(), clientID)) {
                    _mobileClient.setConnection(connection);
                    return true;
                }
            default:
                return false;
        }
    }

    public boolean isTeamReady() {
        return _mobileClient.isReady() && _unityClient.isReady();
    }

    public boolean belongsToTeam(ClientID clientID) {
        return Objects.equals(_mobileClient.getClientID(), clientID) ||
                Objects.equals(_unityClient.getClientID(), clientID);
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
            _mobileClient.setConnection(null);
            _mobileClient.setIsReady(false);
        }

        if(_unityClient != null && Objects.equals(_unityClient.getConnection(), connection)) {
            _unityClient.setConnection(null);
            _unityClient.setIsReady(false);
        }
    }

    public void setPuzzle(Puzzle puzzle) {
        _puzzleManager.setPuzzle(puzzle);
    }

    public void setQuestionList(List<Question> questionList) {
        _questionManager.setQuestionList(questionList);
    }

    public boolean questionsAvailable() {
        return _questionManager.questionsAvailable();
    }

    public boolean puzzlePartsToFindAvailable() {
        return _puzzleManager.partsAvailable();
    }
}
