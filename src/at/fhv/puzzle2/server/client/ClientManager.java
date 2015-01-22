package at.fhv.puzzle2.server.client;

import at.fhv.puzzle2.communication.ClientID;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;

import java.util.LinkedList;
import java.util.List;
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

    public boolean areAllReady() {
        return _team1.isTeamReady() && _team2.isTeamReady();
    }

    public boolean belongsToAnyTeam(CommandConnection connection) {
        return getTeamOfClient(connection) != null;
    }

    public boolean belongsToAnyTeam(ClientID clientID) {
        return getTeamOfClient(clientID) != null;
    }

    public Team getTeamOfClient(ClientID clientID) {
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

    public Client getClientByID(ClientID clientID) {
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

    public ClientID registerNewClient(ClientType type, CommandConnection connection) {
        if(type == ClientType.Configurator) {
            if(_configurator == null) {
                ClientID randomID = ClientID.createRandomClientID();
                _configurator = new Client(connection, randomID);

                return randomID;
            }

            return null;
        }

        ClientID clientID = null;
        clientID = _team1.registerNewClient(type, connection);

        if(clientID != null) {
            return clientID;
        }

        clientID = _team2.registerNewClient(type, connection);

        return clientID;
    }

    public boolean registerReconnectedClient(ClientType type, CommandConnection connection, ClientID clientID) {
        if(type == ClientType.Configurator) {
            if(_configurator == null) {
                _configurator = new Client(connection, clientID);
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
        if(_configurator != null && _configurator.getConnection().equals(connection)) {
            _configurator = null;
        } else {
            _team1.clientDisconnected(connection);
            _team2.clientDisconnected(connection);
        }
    }

    public List<CommandConnection> getListOfAllConnections() {
        List<CommandConnection> connectionList = new LinkedList<>();
        if(_configurator != null) {
            connectionList.add(_configurator.getConnection());
        }

        connectionList.addAll(_team1.getConnections());
        connectionList.addAll(_team2.getConnections());

        return connectionList;
    }
}
