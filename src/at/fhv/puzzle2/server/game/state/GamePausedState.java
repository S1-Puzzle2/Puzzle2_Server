package at.fhv.puzzle2.server.game.state;

import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.command.commands.*;
import at.fhv.puzzle2.server.SendQueue;
import at.fhv.puzzle2.server.game.Game;
import at.fhv.puzzle2.server.users.ClientManager;
import at.fhv.puzzle2.server.users.client.Client;
import at.fhv.puzzle2.server.users.client.state.ReadyClientState;

import java.util.List;

public class GamePausedState extends PreGameRunningState {
    public GamePausedState(Game game, ClientManager clientManager) {
        super(game, clientManager);
    }

    @Override
    public GameState processCommand(Command command) {
        if(command instanceof ReadyCommand || command instanceof  RegisterCommand ||
                command instanceof GetGameStateCommand || command instanceof GetPuzzlePartCommand) {
            return super.processCommand(command);
        }

        return null;
    }

    @Override
    public boolean commandAllowedInGameState(Command command) {
        return isClassOf(command, RegisterCommand.class,
                GetGameStateCommand.class, GetPuzzlePartCommand.class);
    }

    @Override
    public void enter(GameState lastState) {
        //Alright, so one of the clients disconnected. Now notify all other clients that the game
        //has paused and set their status to Ready
        List<Client> clientList = _clientManager.getAllClients();

        for(Client client: clientList) {
            client.swapClientState(new ReadyClientState(client), true);
            PauseCommand pauseCommand = new PauseCommand(client.getClientID());
            pauseCommand.setConnection(client.getConnection());

            SendQueue.getInstance().addCommandToSend(pauseCommand);
        }
    }
}