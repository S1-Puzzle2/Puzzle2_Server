package at.fhv.puzzle2.server.client;

import at.fhv.puzzle2.communication.application.connection.CommandConnection;

import java.util.Objects;
import java.util.UUID;

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

    public boolean areAllReady() {
        return _team1.isTeamReady() && _team2.isTeamReady();
    }

    public boolean belongsToAnyTeam(CommandConnection connection) {
        return getTeamOfClient(connection) != null;
    }

    public boolean belongsToAnyTeam(String clientID) {
        return getTeamOfClient(clientID) != null;
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

    public Team getTeamOfClient(CommandConnection connection) {
        if(_team1.belongsToTeam(connection)) {
            return _team1;
        }

        if(_team2.belongsToTeam(connection)) {
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

    public String registerNewClient(ClientType type, CommandConnection connection) {
        if(type == ClientType.Configurator) {
            if(_configurator == null) {
                String uuid = UUID.randomUUID().toString();
                _configurator = new Client(ClientType.Configurator, connection, uuid);

                return uuid;
            }

            return null;
        }

        String uuid = null;
        uuid = _team1.registerNewClient(type, connection);

        if(uuid != null) {
            return uuid;
        }

        uuid = _team2.registerNewClient(type, connection);

        return uuid;
    }

    public boolean registerReconnectedClient(ClientType type, CommandConnection connection, String clientID) {
        if(type == ClientType.Configurator) {
            if(_configurator == null) {
                _configurator = new Client(type, connection, clientID);
                return true;
            }

            return false;
        }

        if(_team1.registerReconnectedClient(type, connection, clientID)) {
            return true;
        }

        if(_team2.registerReconnectedClient(type, connection, clientID)) {
            return true;
        }

        return false;
    }

    public void clientDisconnected(CommandConnection connection) {
        if(_configurator.getConnection().equals(connection)) {
            _configurator = null;
        } else {
            _team1.clientDisconnected(connection);
            _team2.clientDisconnected(connection);
        }
    }
}
