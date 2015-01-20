package at.fhv.puzzle2.server.logic;

import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.command.commands.NotAllowedCommand;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.server.client.ClientManager;
import at.fhv.puzzle2.server.entity.Puzzle;
import at.fhv.puzzle2.server.state.BeforeGameStartState;
import at.fhv.puzzle2.server.state.GameState;
import at.fhv.puzzle2.server.util.CommandSender;

public class Game {
    GameState _currentState;
    private ClientManager _clientManager;
    private Puzzle _puzzle = null;

    public Game(ClientManager clientManager) {
        _currentState = new BeforeGameStartState(clientManager, this);
    }

    public void setGameState(GameState state) {
        _currentState = state;
    }

    public GameState getGameState() {
        return _currentState;
    }

    public void processCommand(Command command) {
        System.out.println("Processing command " + command.getCommandType().getTypeString());
        if(_currentState.commandAllowedInGameState(command)) {
            _currentState.processCommand(command);
        } else {
            NotAllowedCommand notAllowedCommand = new NotAllowedCommand(command.getClientID());
            notAllowedCommand.setCommandType(command.getCommandType());

            CommandSender.sendCommandInThread(command.getConnection(), notAllowedCommand);
        }
    }

    public void processDisconnectedConnection(CommandConnection connection) {
        _currentState.processDisconnectedClient(connection);
    }

    public boolean commandAllowed(Command command) {
        return _currentState.commandAllowedInGameState(command);
    }

    public void timeElapsed(int time) {

    }

    public Puzzle getPuzzle() {
        return _puzzle;
    }

    public void setPuzzle(Puzzle puzzle) {
        _puzzle = puzzle;
    }
}
