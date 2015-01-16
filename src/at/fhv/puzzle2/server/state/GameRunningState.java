package at.fhv.puzzle2.server.state;

import at.fhv.puzzle2.communication.application.command.AbstractCommand;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.server.logic.Game;

public class GameRunningState extends GameState {

    public GameRunningState(Game game) {
        super(game);
    }

    @Override
    public void processCommand(AbstractCommand command) {
        //TODO
    }

    @Override
    public void processDisconnectedClient(CommandConnection connection) {
        //TODO
    }

    @Override
    public boolean commandAllowedInGameState(AbstractCommand command) {
        //TODO
        return isClassOf(command.getClass());
    }
}
