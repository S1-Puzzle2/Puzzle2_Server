package at.fhv.puzzle2.server.client;

import at.fhv.puzzle2.communication.ClientID;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;

public class ConfiguratorClient extends Client {
    public ConfiguratorClient(CommandConnection connection, ClientID clientID) {
        super(ClientType.Unity, connection, clientID);
    }
}
