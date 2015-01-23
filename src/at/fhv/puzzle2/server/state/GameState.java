package at.fhv.puzzle2.server.state;

import at.fhv.puzzle2.communication.ClientID;
import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.command.commands.RegisteredCommand;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.server.users.ClientManager;
import at.fhv.puzzle2.server.logic.Game;

public abstract class GameState {
    protected Game _game;
    protected ClientManager _clientManager;

    public GameState(Game game, ClientManager clientManager) {
        _game = game;
        _clientManager = clientManager;
    }

    public abstract void processCommand(Command command);
    public abstract void processDisconnectedClient(CommandConnection connection);
    public abstract boolean commandAllowedInGameState(Command command);

    protected boolean isClassOf(Object command, Class<?>... classes) {
        for(Class tmpClass : classes) {
            if(tmpClass.isInstance(command)) {
                return true;
            }
        }

        return false;
    }

    protected RegisteredCommand createRegisterCommand(final CommandConnection connection, ClientID clientID, boolean isRegistered) {
        RegisteredCommand registeredCommand = new RegisteredCommand(clientID);
        registeredCommand.setRegistered(isRegistered);
        registeredCommand.setConnection(connection);

        return registeredCommand;
    }

    protected RegisteredCommand sendRegisteredCommand(CommandConnection connection, boolean isRegistered) {
        return createRegisterCommand(connection, null, isRegistered);
    }
}
