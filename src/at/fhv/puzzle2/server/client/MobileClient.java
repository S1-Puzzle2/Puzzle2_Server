package at.fhv.puzzle2.server.client;


import at.fhv.puzzle2.communication.ClientID;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;

public class MobileClient extends Client {
    public MobileClient(CommandConnection connection, ClientID clientID) {
        super(connection, clientID);
    }
}
