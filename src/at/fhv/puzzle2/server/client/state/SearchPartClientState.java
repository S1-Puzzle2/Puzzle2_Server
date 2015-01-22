package at.fhv.puzzle2.server.client.state;

import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.server.client.Client;

public class SearchPartClientState extends ClientState {
    public SearchPartClientState(Client client) {
        super(client);
    }

    @Override
    public ClientState handleCommand(Command command) {
        return null;
    }
}
