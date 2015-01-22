package at.fhv.puzzle2.server.client.state;

import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.command.commands.RegisteredCommand;
import at.fhv.puzzle2.server.SendQueue;
import at.fhv.puzzle2.server.client.Client;
import at.fhv.puzzle2.server.client.MobileClient;
import at.fhv.puzzle2.server.client.UnityClient;

import java.util.Objects;

public class NotConnectedCientState extends ClientState {
    public NotConnectedCientState(Client client) {
        super(client);
    }

    @Override
    public ClientState handleCommand(Command command) {
        if(!(command instanceof RegisteredCommand)) {
            return null;
        }

        RegisteredCommand registeredCommand = new RegisteredCommand(command.getClientID());

        if(_client instanceof MobileClient) {
            if(Objects.equals(_client.getClientID(), command.getClientID())) {
                _client.setConnection(command.getConnection());
                _client.swapClientState(new NotReadyClientState(_client));

                registeredCommand.setRegistered(true);
            }
        } else if(_client instanceof UnityClient) {
            if(!_client.isConnected()) {
                _client.setConnection(command.getConnection());
                _client.swapClientState(new NotReadyClientState(_client));

                registeredCommand.setRegistered(true);
            }

            registeredCommand.setRegistered(false);

        }

        SendQueue.getInstance().addCommandToSend(registeredCommand);

        return null;
    }
}
