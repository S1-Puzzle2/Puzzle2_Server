package at.fhv.puzzle2.server.users.client.state;

import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.server.users.client.Client;

import java.util.Optional;

public abstract class ClientState {
    protected Client _client;

    ClientState(Client client) {
        _client = client;
    }

    public void setClient(Client client) {
        _client = client;
    }

    public Optional<ClientState> handleCommand(Command command) {
        return Optional.empty();
    }

    public void enter() {
        //Doesnt have to be implemented by all states
    }
}
