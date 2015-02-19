package at.fhv.puzzle2.server.game.state;

import at.fhv.puzzle2.communication.ClientID;
import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.command.commands.*;
import at.fhv.puzzle2.communication.application.command.commands.unity.ShowQRCommand;
import at.fhv.puzzle2.server.SendQueue;
import at.fhv.puzzle2.server.database.Database;
import at.fhv.puzzle2.server.entity.PuzzlePart;
import at.fhv.puzzle2.server.entity.manager.PuzzleEntityManager;
import at.fhv.puzzle2.server.game.Game;
import at.fhv.puzzle2.server.users.ClientManager;
import at.fhv.puzzle2.server.users.Team;
import at.fhv.puzzle2.server.users.client.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toCollection;

public abstract class PreGameRunningState extends GameState {
    PreGameRunningState(Game game, ClientManager clientManager) {
        super(game, clientManager);
    }

    @Override
    public Optional<GameState> processCommand(Command command) {
        Client client = _clientManager.getClientByID(command.getClientID());

        if(command instanceof ReadyCommand) {
            client.processCommand(command);

            if(_clientManager.areAllReady() && _game.getPuzzle() != null) {
                return Optional.of(new GameRunningState(_game, _clientManager));
            }
        } else if(command instanceof RegisterCommand) {
            RegisterCommand registerCommand = (RegisterCommand) command;

            Optional<ClientType> type = ClientType.getClientTypeByString(registerCommand.getClientType());
            Client newClient = null;
            if(!type.isPresent()) {
                return Optional.empty();
            }

            switch (type.get()) {
                case Unity:
                    newClient = new UnityClient(registerCommand.getConnection());
                    break;
                case Mobile:
                    newClient = new MobileClient(registerCommand.getConnection());
                    break;
                case Configurator:
                    newClient = new ConfiguratorClient(registerCommand.getConnection());
                    break;
            }

            if(registerCommand.getClientID() != null) {
                newClient.setID(registerCommand.getClientID());
            } else {
                newClient.setID(ClientID.createRandomClientID());
            }

            boolean registered;

            if(registerCommand.getClientID() != null) {
                //So he tries to connect with a clientID, lets reassign the connection
                registered = _clientManager.registerReconnectedClient(newClient);
            } else {
                registered = _clientManager.registerNewClient(newClient);
            }

            RegisteredCommand registeredCommand = new RegisteredCommand(newClient.getClientID());
            registeredCommand.setConnection(newClient.getConnection());
            registeredCommand.setRegistered(registered);

            SendQueue.getInstance().addCommandToSend(registeredCommand);

            if(newClient instanceof UnityClient && registered) {
                ShowQRCommand qrCommand = new ShowQRCommand(newClient.getClientID());

                Team team = _clientManager.getTeamOfClient(newClient);


                qrCommand.setQRCode(team.getMobileClientID().toString());
                qrCommand.setConnection(newClient.getConnection());

                SendQueue.getInstance().addCommandToSend(qrCommand);
            }
        } else if(command instanceof GetGameStateCommand) {
            GameStateCommand gameStateCommand = new GameStateCommand(command.getClientID());
            gameStateCommand.setConnection(command.getConnection());

            if(_game.getPuzzle() != null) {
                gameStateCommand.setPuzzleName(_game.getPuzzle().getName());

                List<Integer> idList = _game.getPuzzle().getPartsList().stream().mapToInt(PuzzlePart::getID).boxed().collect(toCollection(LinkedList::new));

                gameStateCommand.setPartsList(idList);
            }

            gameStateCommand.setTeamName(_clientManager.getTeamOfClient(client).getTeamName());

            SendQueue.getInstance().addCommandToSend(gameStateCommand);
        } else if(command instanceof GetPuzzlePartCommand) {
            PuzzlePartCommand puzzlePartCommand = new PuzzlePartCommand(command.getClientID());
            puzzlePartCommand.setConnection(command.getConnection());


            Optional<PuzzlePart> partOptional = Optional.empty();
            if(_game.getPuzzle() != null) {
                partOptional = _game.getPuzzle().getPartByID(((GetPuzzlePartCommand) command).getPuzzlePartID());
            }

            if(!partOptional.isPresent()) {
                PuzzleEntityManager puzzleEntityManager = new PuzzleEntityManager(Database.getInstance());
                partOptional = puzzleEntityManager.getPuzzlePartByID(((GetPuzzlePartCommand) command).getPuzzlePartID());
            }

            if(partOptional.isPresent()) {
                PuzzlePart part = partOptional.get();
                puzzlePartCommand.setImageID(part.getID());
                puzzlePartCommand.setOrder(part.getOrder());
                puzzlePartCommand.setImage(part.getImage());

                SendQueue.getInstance().addCommandToSend(puzzlePartCommand);
            }
        }
        return Optional.empty();
    }
}
