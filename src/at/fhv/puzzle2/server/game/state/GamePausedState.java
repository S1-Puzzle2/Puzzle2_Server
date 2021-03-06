package at.fhv.puzzle2.server.game.state;

import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.command.commands.*;
import at.fhv.puzzle2.server.SendQueue;
import at.fhv.puzzle2.server.game.Game;
import at.fhv.puzzle2.server.users.ClientManager;
import at.fhv.puzzle2.server.users.client.Client;

import java.util.List;
import java.util.Optional;

public class GamePausedState extends PreGameRunningState {
    public GamePausedState(Game game, ClientManager clientManager) {
        super(game, clientManager);
    }

    @Override
    public Optional<GameState> processCommand(Command command) {
        return super.processCommand(command);
    }

    @Override
    public boolean commandAllowedInGameState(Command command) {
        return isClassOf(command, RegisterCommand.class,
                GetGameInfoCommand.class, RegisterGameStatusListenerCommand.class, GetPuzzlePartCommand.class,
                RegisterGameStatusListenerCommand.class,
                ReadyCommand.class);
    }

    @Override
    public void enter(GameState lastState) {
        //Alright, so one of the clients disconnected. Now notify all other clients that the game
        //has paused and set their status to Ready

        /* Only store the state of still connected clients, the disconnected client has already changed its state
           in team.clientDisconnected() */
        List<Client> clientList = _clientManager.getConnectedClients();

        for(Client client: clientList) {
            client.gamePaused();

            GamePausedCommand gamePausedCommand = new GamePausedCommand(client.getClientID());
            gamePausedCommand.setConnection(client.getConnection());

            SendQueue.getInstance().addCommandToSend(gamePausedCommand);
        }
    }
}
