package at.fhv.puzzle2.server.users.client;

import at.fhv.puzzle2.communication.ClientID;
import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.server.users.client.state.ClientState;
import at.fhv.puzzle2.server.users.client.state.SolvePuzzleClientState;

import java.util.Optional;

public class UnityClient extends Client {
    public UnityClient(CommandConnection connection, ClientID clientID) {
        super(connection, clientID);
    }

    public UnityClient(CommandConnection connection) {
        super(connection, null);
    }

    @Override
    public void processCommand(Command command) {
        Optional<ClientState> state = _currentState.handleCommand(command);

        if(state.isPresent()) {
            swapClientState(state.get());
        }
    }

    @Override
    public ClientState fillDataInState(ClientState state) {
        if(state instanceof SolvePuzzleClientState) {
            SolvePuzzleClientState solvePuzzleClientState = (SolvePuzzleClientState) state;
            solvePuzzleClientState.setPuzzleManager(_team.getPuzzleManager());
        }

        return state;
    }

    @Override
    protected ClientState getStartingState() {
        ClientState state = new SolvePuzzleClientState(this);

        return this.fillDataInState(state);
    }
}
