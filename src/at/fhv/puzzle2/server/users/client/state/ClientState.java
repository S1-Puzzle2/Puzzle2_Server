package at.fhv.puzzle2.server.users.client.state;

import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.server.users.client.Client;

public abstract class ClientState {
    final Client _client;

    ClientState(Client client) {
        _client = client;
    }

    public abstract ClientState handleCommand(Command command);

    public void enter() {
        //Doesnt have to be implemented by all states
    }
}
