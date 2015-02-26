package at.fhv.puzzle2.server.users.client.state;

import at.fhv.puzzle2.communication.application.command.commands.mobile.AllPartsUnlockedCommand;
import at.fhv.puzzle2.server.SendQueue;
import at.fhv.puzzle2.server.users.client.Client;

public class AllPartsUnlockedClientState extends ClientState {
    public AllPartsUnlockedClientState(Client client) {
        super(client);
    }

    @Override
    public void enter() {
        AllPartsUnlockedCommand allPartsUnlockedCommand = new AllPartsUnlockedCommand(_client.getClientID());
        allPartsUnlockedCommand.setConnection(_client.getConnection());

        SendQueue.getInstance().addCommandToSend(allPartsUnlockedCommand, 3000);
    }

    @Override
    public String toString() {
        return "AllPartsUnlocked";
    }
}
