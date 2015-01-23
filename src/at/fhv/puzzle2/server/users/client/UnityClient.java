package at.fhv.puzzle2.server.users.client;

import at.fhv.puzzle2.communication.ClientID;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;

public class UnityClient extends Client {
    public UnityClient(CommandConnection connection, ClientID clientID) {
        super(ClientType.Unity, connection, clientID);
    }
}
