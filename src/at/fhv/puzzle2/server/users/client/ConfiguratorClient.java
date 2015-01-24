package at.fhv.puzzle2.server.users.client;

import at.fhv.puzzle2.communication.ClientID;
import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.server.users.client.state.ClientState;
import at.fhv.puzzle2.server.users.client.state.NotReadyClientState;

public class ConfiguratorClient extends Client {
    public ConfiguratorClient(CommandConnection connection, ClientID clientID) {
        super(connection, clientID);
    }

    public ConfiguratorClient(CommandConnection connection) {
        super(connection, null);
    }

    @Override
    public void processCommand(Command command) {
        //Nothing to do here
    }

    @Override
    public ClientState fillDataInState(ClientState state) {
        return state;
    }

    @Override
    protected ClientState getStartingState() {
        return new NotReadyClientState(this);
    }
}
