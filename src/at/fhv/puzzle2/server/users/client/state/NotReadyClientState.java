package at.fhv.puzzle2.server.users.client.state;

import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.command.commands.ReadyCommand;
import at.fhv.puzzle2.server.users.client.Client;

import java.util.Optional;

public class NotReadyClientState extends ClientState {
    public NotReadyClientState(Client client) {
        super(client);
    }

    @Override
    public Optional<ClientState> handleCommand(Command command) {
        if(!(command instanceof ReadyCommand)) {
            return Optional.empty();
        }

        _client.swapClientState(new ReadyClientState(_client));

        return Optional.empty();
    }
}
