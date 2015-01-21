package at.fhv.puzzle2.server.state;

import at.fhv.puzzle2.communication.ClientID;
import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.command.commands.GetGameStateCommand;
import at.fhv.puzzle2.communication.application.command.commands.GetPuzzlePartCommand;
import at.fhv.puzzle2.communication.application.command.commands.ReadyCommand;
import at.fhv.puzzle2.communication.application.command.commands.RegisterCommand;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.server.client.ClientManager;
import at.fhv.puzzle2.server.client.ClientType;
import at.fhv.puzzle2.server.logic.Game;

public class BeforeGameStartState extends GameState {
    private final ClientManager _clientManager;

    public BeforeGameStartState(ClientManager clientManager, Game game) {
        super(game);
        _clientManager = clientManager;
    }

    @Override
    public void processCommand(Command command) {
        if(command instanceof ReadyCommand) {
             _clientManager.getClientByID(command.getClientID()).setIsReady(true);

            if(_clientManager.areAllReady()) {
                //TODO we should switch the GameState now and send GameStart commands
                System.out.println("All ready :O");
            }
        } else if(command instanceof RegisterCommand) {
            RegisterCommand registerCommand = (RegisterCommand) command;

            final CommandConnection clientConnection = registerCommand.getConnection();
            ClientType type = ClientType.getClientTypeByString(registerCommand.getClientType());

            if(registerCommand.getClientID() != null) {
                //So he tries to connect with a clientID, lets reassign the connection
                boolean registered = _clientManager.registerReconnectedClient(type, clientConnection, registerCommand.getClientID());
                sendRegisteredCommand(clientConnection, registerCommand.getClientID(), registered);
            }

            ClientID clientID = _clientManager.registerNewClient(type, clientConnection);

            if(clientID == null) {
                sendRegisteredCommand(clientConnection, false);
            } else {
                sendRegisteredCommand(clientConnection, true);
            }
        }
    }

    @Override
    public void processDisconnectedClient(CommandConnection connection) {
        _clientManager.clientDisconnected(connection);
    }

    @Override
    public boolean commandAllowedInGameState(Command command) {
        return isClassOf(command.getClass(),
                RegisterCommand.class, GetGameStateCommand.class, ReadyCommand.class,
                GetPuzzlePartCommand.class
        );
    }
}
