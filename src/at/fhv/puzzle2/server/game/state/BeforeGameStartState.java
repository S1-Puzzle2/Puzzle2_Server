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
import at.fhv.puzzle2.server.database.controller.PuzzleDbController;
import at.fhv.puzzle2.server.database.controller.PuzzlePartDbController;
import at.fhv.puzzle2.server.entity.Puzzle;
import at.fhv.puzzle2.server.entity.PuzzlePart;
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
            PuzzleDbController puzzleDbController = Database.getInstance().getPuzzleController();
            Puzzle puzzle = new Puzzle(((CreatePuzzleCommand) command).getPuzzleName());

            try {
                puzzleDbController.persistPuzzle(puzzle);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else if(command instanceof CreatePuzzlePartCommand) {
            CreatePuzzlePartCommand createPuzzlePartCommand = (CreatePuzzlePartCommand) command;
            PuzzlePart part = new PuzzlePart(createPuzzlePartCommand.getUuid(), createPuzzlePartCommand.getOrder());
            part.setImage(((CreatePuzzlePartCommand) command).getImage());

            PuzzleDbController puzzleDbController = Database.getInstance().getPuzzleController();
            PuzzlePartDbController puzzlePartDbController = Database.getInstance().getPuzzlePartController();
            try {
                Puzzle puzzle = puzzleDbController.getPuzzleByName(((CreatePuzzlePartCommand) command).getPuzzleName());
                puzzlePartDbController.persistPuzzlePart(part, puzzle);
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
