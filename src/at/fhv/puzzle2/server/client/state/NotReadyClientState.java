package at.fhv.puzzle2.server.client.state;

import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.command.commands.ReadyCommand;
import at.fhv.puzzle2.server.client.Client;

public class NotReadyClientState extends ClientState {
    public NotReadyClientState(Client client) {
        super(client);
    }

    @Override
    public ClientState handleCommand(Command command) {
        if(!(command instanceof ReadyCommand)) {
            return null;
        }

        _client.swapClientState(new ReadyClientState(_client));

        return null;
    }
}
