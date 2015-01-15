package at.fhv.puzzle2.server.logic;

import at.fhv.puzzle2.communication.application.command.AbstractCommand;
import at.fhv.puzzle2.server.client.ClientManager;

public abstract class GameState {
    public abstract void processCommand(AbstractCommand command);
    public abstract boolean commandAllowedInGameState(AbstractCommand command);

    protected boolean isClassOf(Class<?> commandClass, Class<?>... classes) {
        for(Class tmpClass : classes) {
            if(commandClass.isInstance(tmpClass)) {
                return true;
            }
        }

        return false;
    }
}
