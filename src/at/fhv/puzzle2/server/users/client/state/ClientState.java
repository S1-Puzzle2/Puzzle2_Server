package at.fhv.puzzle2.server.users.client.state;

import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.server.users.Team;
import at.fhv.puzzle2.server.users.client.Client;

public abstract class ClientState {
    protected Client _client;

    public ClientState(Client client) {
        _client = client;
    }

    public abstract ClientState handleCommand(Command command);
}