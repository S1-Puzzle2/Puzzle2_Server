package at.fhv.puzzle2.server.users.client.state;

import at.fhv.puzzle2.server.users.client.Client;

public class ReadyClientState extends ClientState {
    public ReadyClientState(Client client) {
        super(client);
    }

    @Override
    public String toString() {
        return "Ready";
    }
}
