package at.fhv.puzzle2.server.state;

import at.fhv.puzzle2.communication.ClientID;
import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.command.commands.RegisteredCommand;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.server.logic.Game;

public abstract class GameState {
    private Game _game;

    public GameState(Game game) {
        _game = game;
    }

    public abstract void processCommand(Command command);
    public abstract void processDisconnectedClient(CommandConnection connection);
    public abstract boolean commandAllowedInGameState(Command command);

    protected boolean isClassOf(Class<?> commandClass, Class<?>... classes) {
        for(Class tmpClass : classes) {
            if(commandClass.isInstance(tmpClass)) {
                return true;
            }
        }

        return false;
    }

    protected void sendRegisteredCommand(final CommandConnection connection, ClientID clientID, boolean isRegistered) {
        RegisteredCommand registeredCommand = new RegisteredCommand(clientID);
        registeredCommand.setRegistered(isRegistered);

        sendCommandInThread(connection, registeredCommand);
    }

    protected void sendRegisteredCommand(CommandConnection connection, boolean isRegistered) {
        sendRegisteredCommand(connection, null, isRegistered);
    }

    protected void sendCommandInThread(final CommandConnection connection, final Command command) {
        new Thread(() -> connection.sendCommand(command));
    }
}
