package at.fhv.puzzle2.server.state;

import at.fhv.puzzle2.communication.application.command.AbstractCommand;
import at.fhv.puzzle2.communication.application.command.commands.RegisteredCommand;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.server.client.ClientManager;
import at.fhv.puzzle2.server.logic.Game;

public abstract class GameState {
    private Game _game;

    public GameState(Game game) {
        _game = game;
    }

    public abstract void processCommand(AbstractCommand command);
    public abstract void processDisconnectedClient(CommandConnection connection);
    public abstract boolean commandAllowedInGameState(AbstractCommand command);

    protected boolean isClassOf(Class<?> commandClass, Class<?>... classes) {
        for(Class tmpClass : classes) {
            if(commandClass.isInstance(tmpClass)) {
                return true;
            }
        }

        return false;
    }

    protected void sendRegisteredCommand(final CommandConnection connection, String clientID, boolean isRegistered) {
        final RegisteredCommand registeredCommand = new RegisteredCommand(clientID);
        registeredCommand.setRegistered(isRegistered);

        new Thread(() -> connection.sendCommand(registeredCommand));
    }
}
