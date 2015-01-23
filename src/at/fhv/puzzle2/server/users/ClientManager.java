package at.fhv.puzzle2.server.users;

import at.fhv.puzzle2.communication.ClientID;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.server.entity.Puzzle;
import at.fhv.puzzle2.server.logic.manager.QuestionManager;
import at.fhv.puzzle2.server.users.client.Client;
import at.fhv.puzzle2.server.users.client.ClientType;

import java.util.Objects;

public class ClientManager {
    private Team _team1;
    private Team _team2;

    private Client _configurator;

    public ClientManager(QuestionManager questionManager) {
        _team1 = new Team("Team1", (QuestionManager) questionManager.clone());
        _team2 = new Team("Team2", (QuestionManager) questionManager.clone());
    }

    public void setPuzzle(Puzzle puzzle) {
        _team1.setPuzzle(puzzle);
        _team2.setPuzzle(puzzle);
    }

    public boolean areAllReady() {
        return _team1.isTeamReady() && _team2.isTeamReady();
    }

    public boolean belongsToAnyTeam(ClientID clientID) {
        return _team1.belongsToTeam(clientID) || _team2.belongsToTeam(clientID);
    }

    public Client getClientByID(ClientID clientID) {
        if(_team1.belongsToTeam(clientID)) {
            return _team1.getClientByID(clientID);
        }

        if(_team2.belongsToTeam(clientID)) {
            return _team2.getClientByID(clientID);
        }

        if(_configurator != null && Objects.equals(_configurator.getClientID(), clientID)) {
            return _configurator;
        }

        return null;
    }

    public boolean registerNewClient(Client client) {
        if(client.getClientType() == ClientType.Configurator) {
            if(_configurator == null) {
                _configurator = client;

                return true;
            }
        }

        return _team1.registerNewClient(client) || _team2.registerNewClient(client);
    }

    public boolean registerReconnectedClient(Client client) {
        if(client.getClientType() == ClientType.Configurator) {
            return registerNewClient(client);
        }

        Client tmpClient = getClientByID(client.getClientID());
        if(tmpClient == null || !tmpClient.isConnected()) {
            return _team1.registerReconnectedClient(client) || _team2.registerReconnectedClient(client);
        }

        return false;
    }

    public void clientDisconnected(CommandConnection connection) {
        if(_configurator != null && _configurator.getConnection().equals(connection)) {
            _configurator = null;
        } else {
            _team1.clientDisconnected(connection);
            _team2.clientDisconnected(connection);
        }
    }
}
