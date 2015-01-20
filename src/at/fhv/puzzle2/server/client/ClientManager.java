package at.fhv.puzzle2.server.client;

import at.fhv.puzzle2.communication.ClientID;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;

import java.util.Objects;

public class ClientManager {
    private Team _firstTeam;
    private Team _secondTeam;

    private Client _configurator;

    public ClientManager(Team firstTeam, Team secondTeam) {
        _firstTeam = firstTeam;
        _secondTeam = secondTeam;
    }


    public Client getConfigurator() {
        return _configurator;
    }

    public void setConfigurator(Client configurator) {
        _configurator = configurator;
    }

    public boolean areAllReady() {
        return _firstTeam.isTeamReady() && _secondTeam.isTeamReady();
    }

    public boolean belongsToAnyTeam(CommandConnection connection) {
        return getTeamOfClient(connection) != null;
    }

    public boolean belongsToAnyTeam(ClientID clientID) {
        return getTeamOfClient(clientID) != null;
    }

    public Team getTeamOfClient(ClientID clientID) {
        if(_firstTeam.belongsToTeam(clientID)) {
            return _firstTeam;
        }

        if(_secondTeam.belongsToTeam(clientID)) {
            return _secondTeam;
        }

        return null;
    }

    public Team getTeamOfClient(CommandConnection connection) {
        if(_firstTeam.belongsToTeam(connection)) {
            return _firstTeam;
        }

        if(_secondTeam.belongsToTeam(connection)) {
            return _secondTeam;
        }

        return null;
    }

    public Client getClientByID(ClientID clientID) {
        if(_firstTeam.belongsToTeam(clientID)) {
            return _firstTeam.getClientByID(clientID);
        }

        if(_secondTeam.belongsToTeam(clientID)) {
            return _secondTeam.getClientByID(clientID);
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
                _configurator = new Client(ClientType.Configurator, connection, randomID);

                return randomID;
            }

            return null;
        }

        ClientID clientID = null;
        clientID = _firstTeam.registerNewClient(type, connection);

        if(clientID != null) {
            return clientID;
        }

        clientID = _secondTeam.registerNewClient(type, connection);

        return clientID;
    }

    public boolean registerReconnectedClient(ClientType type, CommandConnection connection, ClientID clientID) {
        if(type == ClientType.Configurator) {
            if(_configurator == null) {
                _configurator = new Client(type, connection, clientID);
                return true;
            }

            return false;
        }

        if(_firstTeam.registerReconnectedClient(type, connection, clientID)) {
            return true;
        }

        if(_secondTeam.registerReconnectedClient(type, connection, clientID)) {
            return true;
        }

        return false;
    }

    public void clientDisconnected(CommandConnection connection) {
        if(_configurator != null && _configurator.getConnection().equals(connection)) {
            _configurator = null;
        } else {
            _firstTeam.clientDisconnected(connection);
            _secondTeam.clientDisconnected(connection);
        }
    }
}
