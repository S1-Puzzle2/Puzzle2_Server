package at.fhv.puzzle2.server.game.state;

import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.command.commands.*;
import at.fhv.puzzle2.communication.application.command.commands.configurator.*;
import at.fhv.puzzle2.communication.application.command.commands.unity.ShowQRCommand;
import at.fhv.puzzle2.communication.application.command.dto.PuzzleListDTO;
import at.fhv.puzzle2.communication.application.command.dto.PuzzlePartDTO;
import at.fhv.puzzle2.server.SendQueue;
import at.fhv.puzzle2.server.database.Database;
import at.fhv.puzzle2.server.database.UncheckedSQLException;
import at.fhv.puzzle2.server.dto_factory.PuzzleListDTOFactory;
import at.fhv.puzzle2.server.dto_factory.PuzzlePartDTOFactory;
import at.fhv.puzzle2.server.entity.Puzzle;
import at.fhv.puzzle2.server.entity.PuzzlePart;
import at.fhv.puzzle2.server.entity.manager.PuzzleEntityManager;
import at.fhv.puzzle2.server.game.Game;
import at.fhv.puzzle2.server.users.ClientManager;
import at.fhv.puzzle2.server.users.client.Client;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class BeforeGameStartState extends PreGameRunningState {
    public BeforeGameStartState(Game game, ClientManager clientManager) {
        super(game, clientManager);
    }

    @Override
    public Optional<GameState> processCommand(Command command) {
        Client client = _clientManager.getClientByID(command.getClientID());

        if(command instanceof ReadyCommand || command instanceof  RegisterCommand ||
                command instanceof GetGameInfoCommand || command instanceof GetPuzzlePartCommand ||
                command instanceof RegisterGameStatusListenerCommand) {
            return super.processCommand(command);

        } else if(command instanceof CreatePuzzleCommand) {
            Puzzle puzzle = new Puzzle(((CreatePuzzleCommand) command).getPuzzleName());

            PuzzleEntityManager puzzleEntityManager = new PuzzleEntityManager(Database.getInstance());

            PuzzleCreatedCommand puzzleCreatedCommand = new PuzzleCreatedCommand(client.getClientID());
            puzzleCreatedCommand.setConnection(command.getConnection());
            try {
                puzzleEntityManager.storePuzzle(puzzle);

                puzzleCreatedCommand.setSuccess(true);
                puzzleCreatedCommand.setPuzzleID(puzzle.getID());
            } catch(UncheckedSQLException e) {
                puzzleCreatedCommand.setSuccess(false);
            }

            SendQueue.getInstance().addCommandToSend(puzzleCreatedCommand);
        }else if(command instanceof CreatePuzzlePartCommand) {
            PuzzleEntityManager puzzleEntityManager = new PuzzleEntityManager(Database.getInstance());

            CreatePuzzlePartCommand createPuzzlePartCommand = (CreatePuzzlePartCommand) command;
            PuzzlePart part = new PuzzlePart(createPuzzlePartCommand.getUuid(), createPuzzlePartCommand.getOrder());
            part.setImage(createPuzzlePartCommand.getImage());

            int puzzleID = createPuzzlePartCommand.getPuzzleID();

            PuzzlePartCreatedCommand puzzlePartCreatedCommand = new PuzzlePartCreatedCommand(client.getClientID());
            puzzlePartCreatedCommand.setConnection(command.getConnection());
            try {
                puzzleEntityManager.storePuzzlePart(part, puzzleID);

                puzzlePartCreatedCommand.setSuccess(true);
                puzzlePartCreatedCommand.setPuzzlePartID(part.getID());
            } catch (UncheckedSQLException e) {
                puzzlePartCreatedCommand.setSuccess(false);
            }

            SendQueue.getInstance().addCommandToSend(puzzlePartCreatedCommand);
        }else if(command instanceof GetPuzzleListCommand) {
            PuzzleListCommand puzzleListCommand = new PuzzleListCommand(command.getClientID());
            puzzleListCommand.setConnection(command.getConnection());

            PuzzleEntityManager puzzleEntityManager = new PuzzleEntityManager(Database.getInstance());

            List<PuzzleListDTO> dtoList = puzzleEntityManager.getPuzzleList().stream().map(PuzzleListDTOFactory::createPuzzleListDTO).collect(toList());
            puzzleListCommand.setPuzzleList(dtoList);

            SendQueue.getInstance().addCommandToSend(puzzleListCommand);

        }else if(command instanceof GetPuzzlePartListCommand) {
            PuzzlePartListCommand puzzlePartListCommand = new PuzzlePartListCommand(client.getClientID());
            puzzlePartListCommand.setConnection(client.getConnection());

            int puzzleID = ((GetPuzzlePartListCommand) command).getPuzzleID();
            PuzzleEntityManager puzzleEntityManager = new PuzzleEntityManager(Database.getInstance());

            Puzzle puzzle = puzzleEntityManager.getPuzzleByID(puzzleID).get();

            puzzlePartListCommand.setPuzzleID(puzzle.getID());

            List<PuzzlePart> partList = puzzle.getPartsList();


            List<PuzzlePartDTO> partDTOList = partList.stream().map(PuzzlePartDTOFactory::createPuzzlePartDTO).collect(toList());

            puzzlePartListCommand.setPartList(partDTOList);

            SendQueue.getInstance().addCommandToSend(puzzlePartListCommand);
        } else if(command instanceof SetPuzzleCommand) {
            if(_game.getPuzzle() == null) {
                PuzzleEntityManager puzzleEntityManager = new PuzzleEntityManager(Database.getInstance());
                Puzzle puzzle = puzzleEntityManager.getPuzzleByID(((SetPuzzleCommand) command).getPuzzleID()).get();
                _game.setPuzzle(puzzle);
            }
        } else {
            client.processCommand(command);
        }

        return Optional.empty();
    }

    @Override
    public boolean commandAllowedInGameState(Command command) {
        return isClassOf(command,
                RegisterCommand.class, GetGameInfoCommand.class, RegisterGameStatusListenerCommand.class, ReadyCommand.class,
                GetPuzzlePartCommand.class, CreatePuzzleCommand.class,
                CreatePuzzlePartCommand.class, ShowQRCommand.class,
                SetPuzzleCommand.class, GetPuzzleListCommand.class,
                RegisterGameStatusListenerCommand.class,
                GetPuzzlePartListCommand.class
        );
    }

    @Override
    public void enter(GameState lastState) {

    }
}
