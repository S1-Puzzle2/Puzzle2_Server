package at.fhv.puzzle2.server.client;

public class ClientManager {
    private Team _team1;
    private Team _team2;

    public ClientManager() {
        _team1 = new Team();
        _team2 = new Team();
    }

    private Client _configurator;

    public Client getConfigurator() {
        return _configurator;
    }

    public void configuratorDisconnected() {
        _configurator = null;
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
}
