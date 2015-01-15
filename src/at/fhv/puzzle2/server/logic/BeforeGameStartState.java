package at.fhv.puzzle2.server.logic;

import at.fhv.puzzle2.communication.application.command.AbstractCommand;
import at.fhv.puzzle2.communication.application.command.commands.*;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.server.client.Client;
import at.fhv.puzzle2.server.client.ClientManager;
import at.fhv.puzzle2.server.client.ClientType;
import at.fhv.puzzle2.server.client.Team;
import org.junit.Before;

import java.util.UUID;

public class BeforeGameStartState extends GameState {
    private final ClientManager _clientManager;

    public BeforeGameStartState(ClientManager clientManager) {
        _clientManager = clientManager;
    }

    @Override
    public void processCommand(AbstractCommand command) {
        if(command instanceof ReadyCommand) {
             _clientManager.getClientByID(command.getClientID()).setIsReady(true);

            if(_clientManager.areAllReady()) {
                //TODO we should switch the GameState now and send GameStart commands
                System.out.println("All ready :O");
            }
        } else if(command instanceof RegisterCommand) {
            RegisterCommand registerCommand = (RegisterCommand) command;

            final CommandConnection clientConnection = registerCommand.getSender();

            if(registerCommand.getClientID() != null) {
                //So he tries to connect with a clientID, lets reassign the connection
                if(_clientManager.belongsToAnyTeam(registerCommand.getClientID())) {
                    Client client = _clientManager.getClientByID(registerCommand.getClientID());
                    client.setConnection(clientConnection);

                    sendRegisteredCommand(clientConnection, registerCommand.getClientID(), true);
                } else {
                    sendRegisteredCommand(clientConnection, registerCommand.getClientID(), false);
                }

                return;
            }

            final String randomClientID = UUID.randomUUID().toString();

            ClientType type = ClientType.getClientTypeByString(registerCommand.getClientType());
            switch (type) {
                case Unity:
                    Client unityClient = new Client(ClientType.Unity, registerCommand.getSender(), randomClientID);

                    if(_clientManager.getFirstTeam().getUnityClient() == null) {
                        _clientManager.getFirstTeam().setUnityClient(unityClient);
                    } else  if(_clientManager.getSecondTeam().getUnityClient() == null) {
                        _clientManager.getSecondTeam().setUnityClient(unityClient);
                    } else {
                        sendRegisteredCommand(clientConnection, "", false);
                        return;
                    }

                    break;
                case Mobile:
                    sendRegisteredCommand(clientConnection, "", false);
                    break;
                case Configurator:
                    if(_clientManager.getConfigurator() == null) {
                        _clientManager.setConfigurator(new Client(ClientType.Configurator, clientConnection, randomClientID));
                        sendRegisteredCommand(clientConnection, randomClientID, true);
                    } else {
                        sendRegisteredCommand(clientConnection, "", false);
                    }
                    break;
            }
        }
    }

    @Override
    public boolean commandAllowedInGameState(AbstractCommand command) {
        return isClassOf(command.getClass(),
                RegisterCommand.class, GetGameStateCommand.class, ReadyCommand.class,
                GetPuzzlePartCommand.class
        );
    }

    private void sendRegisteredCommand(final CommandConnection connection, String clientID, boolean isRegistered) {
        final RegisteredCommand registeredCommand = new RegisteredCommand(clientID);
        registeredCommand.setRegistered(isRegistered);

        new Thread(() -> connection.sendCommand(registeredCommand));
    }
}
