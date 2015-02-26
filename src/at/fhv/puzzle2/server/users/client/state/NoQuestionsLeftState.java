package at.fhv.puzzle2.server.users.client.state;

import at.fhv.puzzle2.communication.application.command.commands.mobile.NoQuestionsLeftCommand;
import at.fhv.puzzle2.server.SendQueue;
import at.fhv.puzzle2.server.users.client.Client;

public class NoQuestionsLeftState extends ClientState {
    public NoQuestionsLeftState(Client client) {
        super(client);
    }

    @Override
    public void enter() {
        NoQuestionsLeftCommand command = new NoQuestionsLeftCommand(this._client.getClientID());

        SendQueue.getInstance().addCommandToSend(command, 3000);
    }

    @Override
    public String toString() {
        return "NoQuestionsLeft";
    }
}
