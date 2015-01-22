package at.fhv.puzzle2.server.state;

import at.fhv.puzzle2.communication.ClientID;
import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.command.commands.*;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.server.SendQueue;
import at.fhv.puzzle2.server.client.Client;
import at.fhv.puzzle2.server.client.ClientManager;
import at.fhv.puzzle2.server.client.ClientType;
import at.fhv.puzzle2.server.team.Team;
import at.fhv.puzzle2.server.client.state.ReadyClientState;
import at.fhv.puzzle2.server.entity.PuzzlePart;
import at.fhv.puzzle2.server.logic.Game;

import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toCollection;

public class BeforeGameStartState extends GameState {
    public BeforeGameStartState(Game game, ClientManager clientManager) {
        super(game, clientManager);
    }

    @Override
    public void processCommand(Command command) {
        if(command instanceof ReadyCommand) {
            Client client = _clientManager.getClientByID(command.getClientID());
             client.swapClientState(new ReadyClientState(client));

            if(_clientManager.areAllReady()) {
                //TODO we should switch the GameState now and send GameStart commands
                System.out.println("All ready :O");
            }
        } else if(command instanceof RegisterCommand) {
            RegisterCommand registerCommand = (RegisterCommand) command;

            final CommandConnection clientConnection = registerCommand.getConnection();
            ClientType type = ClientType.getClientTypeByString(registerCommand.getClientType());

            if(registerCommand.getClientID() != null) {
                //So he tries to connect with a clientID, lets reassign the connection
                boolean registered = _clientManager.registerReconnectedClient(type, clientConnection, registerCommand.getClientID());
                sendRegisteredCommand(clientConnection, registerCommand.getClientID(), registered);

                return;
            }

            ClientID clientID = _clientManager.registerNewClient(type, clientConnection);

            if(clientID == null) {
                sendRegisteredCommand(clientConnection, clientID, false);
            } else {
                sendRegisteredCommand(clientConnection, clientID, true);
            }
        } else if(command instanceof GetGameStateCommand) {
            GameStateCommand gameStateCommand = new GameStateCommand(command.getClientID());
            gameStateCommand.setConnection(command.getConnection());

            Team team = _clientManager.getTeamOfClient(command.getClientID());

            if(team != null) {
                gameStateCommand.setTeamName(team.getTeamName());
            }


            if(_game.getPuzzle() != null) {
                gameStateCommand.setPuzzleName(_game.getPuzzle().getName());

                List<Integer> idList = _game.getPuzzle().getPartsList().stream().mapToInt(PuzzlePart::getID).boxed().collect(toCollection(LinkedList::new));

                gameStateCommand.setPartsList(idList);
            }

            SendQueue.getInstance().addCommandToSend(gameStateCommand);
        }
    }

    @Override
    public void processDisconnectedClient(CommandConnection connection) {
        _clientManager.clientDisconnected(connection);
    }

    @Override
    public boolean commandAllowedInGameState(Command command) {
        return isClassOf(command.getClass(),
                RegisterCommand.class, GetGameStateCommand.class, ReadyCommand.class,
                GetPuzzlePartCommand.class
        );
    }
}
