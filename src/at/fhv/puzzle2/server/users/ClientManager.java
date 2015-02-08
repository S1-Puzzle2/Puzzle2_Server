package at.fhv.puzzle2.server.users;

import at.fhv.puzzle2.communication.ClientID;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.server.Configuration;
import at.fhv.puzzle2.server.entity.Puzzle;
import at.fhv.puzzle2.server.users.client.Client;
import at.fhv.puzzle2.server.users.client.ConfiguratorClient;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ClientManager {
    private final Team _team1;
    private final Team _team2;

    private Client _configurator = null;

    public ClientManager() {
        //Load all team names from the configuration file
        Configuration configuration = Configuration.getInstance();
        List<String> teamNames = configuration.getList("game.team_names", String.class);

        _team1 = createTeam(teamNames, "Team1");
        _team2 = createTeam(teamNames, "Team2");
    }

    private Team createTeam(List<String> teamNames, String defaultName) {
        String teamName = getRandomNameFromList(teamNames);
        if(teamName == null) {
            return new Team(defaultName);
        }

        return new Team(teamName);
    }

    private String getRandomNameFromList(List<String> names) {
        Random random = new Random(System.nanoTime());

        if(names.size() <= 1) {
            return null;
        }

        int nameIndex = random.nextInt(names.size());
        String name = names.get(nameIndex);
        names.remove(nameIndex);

        return name;
    }

    public void gameFinished(Team winningTeam) {
        if(Objects.equals(_team1, winningTeam)) {
            _team1.gameFinished(true);
            _team2.gameFinished(false);
        } else {
            _team1.gameFinished(false);
            _team2.gameFinished(true);
        }

        if(_configurator != null) {
            _configurator.gameFinished();
        }
    }

    public void setPuzzle(Puzzle puzzle) {
        _team1.setPuzzle(puzzle);
        _team2.setPuzzle(puzzle);
    }

    public boolean areAllReady() {
        return _team1.isTeamReady() && _team2.isTeamReady();
    }

    public Team getTeamOfClient(Client client) {
        if(_team1.belongsToTeam(client.getClientID())) {
            return _team1;
        }

        if(_team2.belongsToTeam(client.getClientID())) {
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

        if(_configurator != null && Objects.equals(_configurator.getClientID(), clientID)) {
            return _configurator;
        }

        return null;
    }

    public boolean registerNewClient(Client client) {
        if(client instanceof ConfiguratorClient) {
            if(_configurator == null) {
                _configurator = client;

                return true;
            }
        }

        return _team1.registerNewClient(client) || _team2.registerNewClient(client);
    }

    public boolean registerReconnectedClient(Client client) {
        if(client instanceof ConfiguratorClient) {
            return registerNewClient(client);
        }

        Client tmpClient = getClientByID(client.getClientID());
        if(tmpClient == null || !tmpClient.isConnected()) {
            return _team1.registerReconnectedClient(client) || _team2.registerReconnectedClient(client);
        }

        return false;
    }

    public void clientDisconnected(CommandConnection connection) {
        if(_configurator != null && Objects.equals(_configurator.getConnection(), connection)) {
            _configurator = null;
        } else {
            _team1.clientDisconnected(connection);
            _team2.clientDisconnected(connection);
        }
    }

    public List<Client> getAllClients() {
        List<Client> clientsList = new LinkedList<>();

        if(_configurator != null) {
            clientsList.add(_configurator);
        }

        clientsList.addAll(_team1.getAllClients());
        clientsList.addAll(_team2.getAllClients());

        return clientsList;
    }

    public List<Client> getConnectedClients() {
        List<Client> clientsList = new LinkedList<>();

        if(_configurator != null && _configurator.isConnected()) {
            clientsList.add(_configurator);
        }

        clientsList.addAll(_team1.getConnectedClients());
        clientsList.addAll(_team2.getConnectedClients());

        return clientsList;
    }
}
