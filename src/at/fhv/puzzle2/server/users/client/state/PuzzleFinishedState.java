package at.fhv.puzzle2.server.users.client.state;

import at.fhv.puzzle2.server.users.client.Client;

public class PuzzleFinishedState extends ClientState {
    public PuzzleFinishedState(Client client) {
        super(client);
    }

    @Override
    public String toString() {
        return "PuzzleFinished";
    }
}
