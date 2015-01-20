package at.fhv.puzzle2.server.state;

import at.fhv.puzzle2.communication.ClientID;
import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.command.commands.RegisteredCommand;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.server.logic.Game;
import at.fhv.puzzle2.server.util.CommandSender;

public abstract class GameState {
    private Game _game;

    public GameState(Game game) {
        _game = game;
    }

    public abstract void processCommand(Command command);
    public abstract void processDisconnectedClient(CommandConnection connection);
    public abstract boolean commandAllowedInGameState(Command command);

    protected boolean isClassOf(Object commandClass, Class<?>... classes) {
        for(Class tmpClass : classes) {
            if(tmpClass.isInstance(commandClass)) {
                return true;
            }
        }

        return false;
    }

    protected void sendRegisteredCommand(final CommandConnection connection, ClientID clientID, boolean isRegistered) {
        RegisteredCommand registeredCommand = new RegisteredCommand(clientID);
        registeredCommand.setRegistered(isRegistered);

        CommandSender.sendCommandInThread(connection, registeredCommand);
    }

    protected void sendRegisteredCommand(CommandConnection connection, boolean isRegistered) {
        sendRegisteredCommand(connection, null, isRegistered);
    }
}
