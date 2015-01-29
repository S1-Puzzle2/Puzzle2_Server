package at.fhv.puzzle2.server.game.state;

import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.command.commands.GetGameStateCommand;
import at.fhv.puzzle2.communication.application.command.commands.GetPuzzlePartCommand;
import at.fhv.puzzle2.communication.application.command.commands.ReadyCommand;
import at.fhv.puzzle2.communication.application.command.commands.RegisterCommand;
import at.fhv.puzzle2.communication.application.command.commands.configurator.*;
import at.fhv.puzzle2.communication.application.command.commands.unity.ShowQRCommand;
import at.fhv.puzzle2.server.SendQueue;
import at.fhv.puzzle2.server.database.Database;
import at.fhv.puzzle2.server.entity.Puzzle;
import at.fhv.puzzle2.server.entity.PuzzlePart;
import at.fhv.puzzle2.server.entity.manager.PuzzleEntityManager;
import at.fhv.puzzle2.server.game.Game;
import at.fhv.puzzle2.server.users.ClientManager;
import at.fhv.puzzle2.server.users.client.Client;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toCollection;

public class BeforeGameStartState extends PreGameRunningState {
    public BeforeGameStartState(Game game, ClientManager clientManager) {
        super(game, clientManager);
    }

    @Override
    public GameState processCommand(Command command) {
        Client client = _clientManager.getClientByID(command.getClientID());

        if(command instanceof ReadyCommand || command instanceof  RegisterCommand ||
                command instanceof GetGameStateCommand || command instanceof GetPuzzlePartCommand) {
            return super.processCommand(command);

        } else if(command instanceof CreatePuzzleCommand) {
            Puzzle puzzle = new Puzzle(((CreatePuzzleCommand) command).getPuzzleName());

            PuzzleEntityManager puzzleEntityManager = new PuzzleEntityManager(Database.getInstance());
            try {
                puzzleEntityManager.storePuzzle(puzzle);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else if(command instanceof CreatePuzzlePartCommand) {
            PuzzleEntityManager puzzleEntityManager = new PuzzleEntityManager(Database.getInstance());

            CreatePuzzlePartCommand createPuzzlePartCommand = (CreatePuzzlePartCommand) command;
            PuzzlePart part = new PuzzlePart(createPuzzlePartCommand.getUuid(), createPuzzlePartCommand.getOrder());
            part.setImage(createPuzzlePartCommand.getImage());

            String puzzleName = createPuzzlePartCommand.getPuzzleName();

            try {
                puzzleEntityManager.storePuzzlePart(part, puzzleName);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else if(command instanceof GetPuzzleListCommand) {
            PuzzleListCommand puzzleListCommand = new PuzzleListCommand(command.getClientID());
            puzzleListCommand.setConnection(command.getConnection());

            PuzzleEntityManager puzzleEntityManager = new PuzzleEntityManager(Database.getInstance());
            try {
                List<Puzzle> puzzleList = puzzleEntityManager.getPuzzleList();
                List<String> nameList = puzzleList.stream().map(new Function<Puzzle, String>() {
                    @Override
                    public String apply(Puzzle puzzle) {
                        return puzzle.getName();
                    }
                }).collect(toCollection(LinkedList::new));

                puzzleListCommand.setPuzzleList(nameList);

                SendQueue.getInstance().addCommandToSend(puzzleListCommand);
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }

        }else if(command instanceof GetPuzzlePartListCommand) {
            PuzzlePartListCommand puzzlePartListCommand = new PuzzlePartListCommand(client.getClientID());
            puzzlePartListCommand.setConnection(client.getConnection());

            String puzzleName = ((GetPuzzlePartListCommand) command).getPuzzleName();
            PuzzleEntityManager puzzleEntityManager = new PuzzleEntityManager(Database.getInstance());

            try {
                Puzzle puzzle = puzzleEntityManager.getPuzzleByName(puzzleName);

                puzzlePartListCommand.setPuzzleName(puzzle.getName());

                List<PuzzlePart> partList = puzzle.getPartsList();

                List<PuzzlePartListCommand.DummyPuzzlePart> dummyParts = new LinkedList<>();
                for(PuzzlePart part: partList) {
                    dummyParts.add(puzzlePartListCommand.new DummyPuzzlePart(part.getID(), part.getBarcode()));
                }
                puzzlePartListCommand.setPartList(dummyParts);

                SendQueue.getInstance().addCommandToSend(puzzlePartListCommand);
            } catch (SQLException | IOException e) {
                //TODO
                e.printStackTrace();
            }
        } else if(command instanceof SetPuzzleCommand) {
            PuzzleEntityManager puzzleEntityManager = new PuzzleEntityManager(Database.getInstance());
            try {
                Puzzle puzzle = puzzleEntityManager.getPuzzleByName(((SetPuzzleCommand) command).getPuzzleName());
                _game.setPuzzle(puzzle);
            } catch (SQLException | IOException e) {
                //TODO
                e.printStackTrace();
            }

            return null;

            //} else if(command instanceof SetP)
        } else {
            client.processCommand(command);
        }

        return null;
    }

    @Override
    public boolean commandAllowedInGameState(Command command) {
        return isClassOf(command,
                RegisterCommand.class, GetGameStateCommand.class, ReadyCommand.class,
                GetPuzzlePartCommand.class, CreatePuzzleCommand.class,
                CreatePuzzlePartCommand.class, ShowQRCommand.class,
                SetPuzzleCommand.class, GetPuzzleListCommand.class,
                GetPuzzlePartListCommand.class
        );
    }

    @Override
    public void enter(GameState lastState) {

    }
}
