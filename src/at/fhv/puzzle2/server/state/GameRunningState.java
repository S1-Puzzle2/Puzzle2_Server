package at.fhv.puzzle2.server.state;

import at.fhv.puzzle2.communication.application.command.AbstractCommand;

public class GameRunningState extends GameState {
    @Override
    public void processCommand(AbstractCommand command) {
        //TODO
    }

    @Override
    public boolean commandAllowedInGameState(AbstractCommand command) {
        //TODO
        return isClassOf(command.getClass());
    }
}
