package at.fhv.puzzle2.server.game.state;

import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.command.commands.GetGameStateCommand;
import at.fhv.puzzle2.communication.application.command.commands.GetPuzzlePartCommand;
import at.fhv.puzzle2.communication.application.command.commands.ReadyCommand;
import at.fhv.puzzle2.communication.application.command.commands.RegisterCommand;
import at.fhv.puzzle2.communication.application.command.commands.configurator.CreatePuzzleCommand;
import at.fhv.puzzle2.communication.application.command.commands.configurator.CreatePuzzlePartCommand;
import at.fhv.puzzle2.communication.application.command.commands.unity.ShowQRCommand;
import at.fhv.puzzle2.server.database.Database;
import at.fhv.puzzle2.server.entity.Puzzle;
import at.fhv.puzzle2.server.entity.PuzzlePart;
import at.fhv.puzzle2.server.entity.manager.PuzzleEntityManager;
import at.fhv.puzzle2.server.game.Game;
import at.fhv.puzzle2.server.users.ClientManager;
import at.fhv.puzzle2.server.users.client.Client;

import java.sql.SQLException;

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
        //} else if(command instanceof SetP)
        } else {
            client.processCommand(command);
        }

        return null;
    }

    @Override
    public boolean commandAllowedInGameState(Command command) {
        return isClassOf(command.getClass(),
                RegisterCommand.class, GetGameStateCommand.class, ReadyCommand.class,
                GetPuzzlePartCommand.class, CreatePuzzleCommand.class,
                CreatePuzzlePartCommand.class, ShowQRCommand.class
        );
    }

    @Override
    public void enter(GameState lastState) {

    }
}
