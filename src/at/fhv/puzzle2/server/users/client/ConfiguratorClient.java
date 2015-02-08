package at.fhv.puzzle2.server.users.client;

import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.server.users.Team;
import at.fhv.puzzle2.server.users.client.state.ClientState;
import at.fhv.puzzle2.server.users.client.state.NotReadyClientState;

public class ConfiguratorClient extends Client {
    public ConfiguratorClient(CommandConnection connection) {
        super(connection, null);

        //Set a default team
        _team = new Team(null);
    }

    @Override
    protected ClientState getStartingState() {
        return new NotReadyClientState(this);
    }
}
