package at.fhv.puzzle2.server.dto_factory;

import at.fhv.puzzle2.communication.application.command.dto.PuzzleDTO;
import at.fhv.puzzle2.server.entity.Puzzle;
import at.fhv.puzzle2.server.entity.PuzzlePart;

import java.util.LinkedList;

import static java.util.stream.Collectors.toCollection;

public class PuzzleDTOFactory {
    public static PuzzleDTO createPuzzleDTO(Puzzle puzzle) {
        PuzzleDTO dto = new PuzzleDTO();

        dto.setPartsList(puzzle.getPartsList().stream().mapToInt(PuzzlePart::getID).boxed().collect(toCollection(LinkedList::new)));
        dto.setName(puzzle.getName());

        return dto;
    }
}
