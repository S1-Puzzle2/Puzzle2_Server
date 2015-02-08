package at.fhv.puzzle2.server.users.client;

import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.server.users.client.state.ClientState;
import at.fhv.puzzle2.server.users.client.state.SolvePuzzleClientState;


public class UnityClient extends Client {
    public UnityClient(CommandConnection connection) {
        super(connection, null);
    }

    @Override
    protected ClientState getStartingState() {
        return new SolvePuzzleClientState(this);
    }
}
