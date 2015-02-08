package at.fhv.puzzle2.server.users.client.state;

import at.fhv.puzzle2.communication.application.command.commands.unity.SolvePuzzleCommand;
import at.fhv.puzzle2.server.SendQueue;
import at.fhv.puzzle2.server.entity.PuzzlePart;
import at.fhv.puzzle2.server.logic.manager.PuzzleManager;
import at.fhv.puzzle2.server.users.client.Client;

import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toCollection;

public class SolvePuzzleClientState extends ClientState {
    private PuzzleManager _puzzleManager;

    public SolvePuzzleClientState(Client client) {
        super(client);

        _puzzleManager = _client.getTeam().getPuzzleManager();
    }

    public void enter() {
        SolvePuzzleCommand command = new SolvePuzzleCommand(_client.getClientID());
        command.setConnection(_client.getConnection());

        List<Integer> unlockedIds = _puzzleManager.getFinishedParts().stream()
                .mapToInt(PuzzlePart::getID).boxed()
                .collect(toCollection(LinkedList::new));

        command.setUnlockedPartsList(unlockedIds);

        SendQueue.getInstance().addCommandToSend(command);
    }
}
