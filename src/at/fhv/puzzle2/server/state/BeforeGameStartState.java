package at.fhv.puzzle2.server.state;

import at.fhv.puzzle2.communication.application.command.AbstractCommand;
import at.fhv.puzzle2.communication.application.command.commands.*;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.server.client.Client;
import at.fhv.puzzle2.server.client.ClientManager;
import at.fhv.puzzle2.server.client.ClientType;
import at.fhv.puzzle2.server.logic.Game;

import java.util.UUID;

public class BeforeGameStartState extends GameState {
    private final ClientManager _clientManager;

    public BeforeGameStartState(ClientManager clientManager, Game game) {
        super(game);
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
            ClientType type = ClientType.getClientTypeByString(registerCommand.getClientType());

            if(registerCommand.getClientID() != null) {
                //So he tries to connect with a clientID, lets reassign the connection
                boolean registered = _clientManager.registerReconnectedClient(type, clientConnection, registerCommand.getClientID());
                sendRegisteredCommand(clientConnection, registerCommand.getClientID(), registered);
            }

            final String randomClientID = UUID.randomUUID().toString();

            String uuid = _clientManager.registerNewClient(type, registerCommand.getSender());

            if(uuid == null) {
                sendRegisteredCommand(registerCommand.getSender(), "", false);
            } else {
                sendRegisteredCommand(registerCommand.getSender(), uuid, true);
            }
        }
    }

    @Override
    public void processDisconnectedClient(CommandConnection connection) {
        _clientManager.clientDisconnected(connection);
    }

    @Override
    public boolean commandAllowedInGameState(AbstractCommand command) {
        return isClassOf(command.getClass(),
                RegisterCommand.class, GetGameStateCommand.class, ReadyCommand.class,
                GetPuzzlePartCommand.class
        );
    }
}
