package at.fhv.puzzle2.server.users;

import at.fhv.puzzle2.communication.ClientID;
import at.fhv.puzzle2.communication.application.command.commands.unity.UnlockedPartCommand;
import at.fhv.puzzle2.communication.application.command.dto.TeamStatusDTO;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.server.SendQueue;
import at.fhv.puzzle2.server.StatusChangedListener;
import at.fhv.puzzle2.server.database.Database;
import at.fhv.puzzle2.server.entity.Puzzle;
import at.fhv.puzzle2.server.entity.PuzzlePart;
import at.fhv.puzzle2.server.entity.manager.QuestionEntityManager;
import at.fhv.puzzle2.server.logic.manager.PuzzleManager;
import at.fhv.puzzle2.server.logic.manager.QuestionManager;
import at.fhv.puzzle2.server.users.client.Client;
import at.fhv.puzzle2.server.users.client.MobileClient;
import at.fhv.puzzle2.server.users.client.UnityClient;
import at.fhv.puzzle2.server.users.client.state.AllPartsUnlockedClientState;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Team {
    private final String _teamName;
    private UnityClient _unityClient;
    private MobileClient _mobileClient;

    private boolean _isWinning = false;

    private List<StatusChangedListener> _statusChangedListenerList = new LinkedList<>();

    //We create a ID for the mobile client in advance
    private final ClientID _mobileClientID = ClientID.createRandomClientID();

    private QuestionManager _questionManager;
    private PuzzleManager _puzzleManager;

    public Team(String name) {
        _teamName = name;

        _unityClient = new UnityClient(null);
        _unityClient.setTeam(this);

        _mobileClient = new MobileClient(null);
        _mobileClient.setTeam(this);

        try {
            _questionManager = new QuestionManager(new QuestionEntityManager(Database.getInstance()).loadQuestions());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void gameFinished(boolean isWinning) {
        _isWinning = isWinning;

        _mobileClient.gameFinished();
        _unityClient.gameFinished();
    }

    public void addStatusChangedListener(StatusChangedListener listener) {
        _statusChangedListenerList.add(listener);
    }

    public void statusChanged() {
        for(StatusChangedListener listener : _statusChangedListenerList) {
            listener.statusChanged();
        }
    }

    public boolean isMobileFinished() {
        return _mobileClient.getCurrentState() instanceof AllPartsUnlockedClientState;
    }

    public ClientID getMobileClientID() {
        return _mobileClientID;
    }

    public void partUnlocked(PuzzlePart puzzlePart) {
        _puzzleManager.partFinished(puzzlePart);

        UnlockedPartCommand unlockedPartCommand = new UnlockedPartCommand(_unityClient.getClientID());
        unlockedPartCommand.setConnection(_unityClient.getConnection());
        unlockedPartCommand.setUnlockedPartID(puzzlePart.getID());

        SendQueue.getInstance().addCommandToSend(unlockedPartCommand);
    }

    public void setPuzzle(Puzzle puzzle) {
        _puzzleManager = new PuzzleManager(puzzle);
    }

    public TeamStatusDTO getStatus() {
        TeamStatusDTO dto = new TeamStatusDTO();
        if(_puzzleManager != null) {
            dto.setCountRemainingParts(_puzzleManager.getCountRemainingParts());
        }

        dto.setCountRemainingQuestions(_questionManager.getCountRemainingQuestions());
        dto.setMobileState(_mobileClient.getCurrentState().toString());
        dto.setUnityState(_unityClient.getCurrentState().toString());

        return dto;
    }

    public PuzzleManager getPuzzleManager() {
        return _puzzleManager;
    }

    public QuestionManager getQuestionManager() {
        return _questionManager;
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
        if(newClient instanceof UnityClient  && !_unityClient.isConnected()) {
            newClient.connected(_unityClient);

            _unityClient = (UnityClient) newClient;

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
        if(client instanceof UnityClient && !_unityClient.isConnected() &&
                Objects.equals(_unityClient.getClientID(), client.getClientID())) {
            client.connected(_unityClient);

            _unityClient = (UnityClient) client;

            return true;
        } else if(client instanceof MobileClient && !_mobileClient.isConnected() &&
                    Objects.equals(_mobileClientID, client.getClientID())) {

            client.connected(_mobileClient);

            _mobileClient = (MobileClient) client;

            return true;
        }

        return false;
    }

    public boolean isTeamReady() {
        return _mobileClient.isReady() && _unityClient.isReady();
    }

    public boolean belongsToTeam(ClientID clientID) {
        return Objects.equals(_mobileClient.getClientID(), clientID) || Objects.equals(_unityClient.getClientID(), clientID);
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
        if(Objects.equals(_mobileClient.getConnection(), connection)) {
            _mobileClient.disconnected();
        }

        if(Objects.equals(_unityClient.getConnection(), connection)) {
            _unityClient.disconnected();
        }
    }

    public List<Client> getConnectedClients() {
        List<Client> clientList = new LinkedList<>();
        if(_unityClient.isConnected()) {
            clientList.add(_unityClient);
        }

        if(_mobileClient.isConnected()) {
            clientList.add(_mobileClient);
        }

        return clientList;
    }

    public List<Client> getAllClients() {
        List<Client> clientList = new LinkedList<>();
        clientList.add(_unityClient);
        clientList.add(_mobileClient);

        return clientList;
    }

    public boolean isWinning() {
        return _isWinning;
    }
}
