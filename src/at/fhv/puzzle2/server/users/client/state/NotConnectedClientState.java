package at.fhv.puzzle2.server.users.client.state;

import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.command.commands.RegisteredCommand;
import at.fhv.puzzle2.server.SendQueue;
import at.fhv.puzzle2.server.users.client.Client;

public class NotConnectedClientState extends ClientState {
    public NotConnectedClientState(Client client) {
        super(client);
    }

    @Override
    public ClientState handleCommand(Command command) {
        if(!(command instanceof RegisteredCommand)) {
            return null;
        }

        RegisteredCommand registeredCommand = new RegisteredCommand(command.getClientID());
        _client.setConnection(command.getConnection());
        _client.swapClientState(new NotReadyClientState(_client));

        SendQueue.getInstance().addCommandToSend(registeredCommand);

        return null;
    }
}
