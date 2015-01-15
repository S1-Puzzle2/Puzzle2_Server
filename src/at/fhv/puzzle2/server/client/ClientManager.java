package at.fhv.puzzle2.server.client;

import at.fhv.puzzle2.communication.application.connection.CommandConnection;

import java.util.Objects;

public class ClientManager {
    private Team _team1;
    private Team _team2;

    private Client _configurator;

    public ClientManager() {
        _team1 = new Team("Team1");
        _team2 = new Team("Team2");
    }


    public Client getConfigurator() {
        return _configurator;
    }

    public void setConfigurator(Client configurator) {
        _configurator = configurator;
    }


    public Team getFirstTeam() {
        return _team1;
    }

    public Team getSecondTeam() {
        return _team2;
    }

    public boolean areAllReady() {
        return _team1.isTeamReady() && _team2.isTeamReady();
    }

    public boolean belongsToAnyTeam(CommandConnection connection) {
        return _team1.belongsToTeam(connection) || _team2.belongsToTeam(connection);
    }

    public boolean belongsToAnyTeam(String clientID) {
        return _team1.belongsToTeam(clientID) || _team2.belongsToTeam(clientID);
    }

    public Team getTeamOfClient(String clientID) {
        if(_team1.belongsToTeam(clientID)) {
            return _team1;
        }

        if(_team2.belongsToTeam(clientID)) {
            return _team2;
        }

        return null;
    }

    public Client getClientByID(String clientID) {
        if(_team1.belongsToTeam(clientID)) {
            return _team1.getClientByID(clientID);
        }

        if(_team2.belongsToTeam(clientID)) {
            return _team2.getClientByID(clientID);
        }

        if(Objects.equals(_configurator.getClientID(), clientID)) {
            return _configurator;
        }

        return null;
    }

    public void clientDisconnected(CommandConnection connection) {
        _team1.clientDisconnected(connection);
        _team2.clientDisconnected(connection);
    }
}
