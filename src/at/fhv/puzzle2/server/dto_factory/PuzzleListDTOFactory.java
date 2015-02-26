package at.fhv.puzzle2.server.dto_factory;

import at.fhv.puzzle2.communication.application.command.dto.PuzzleListDTO;
import at.fhv.puzzle2.server.entity.Puzzle;

public class PuzzleListDTOFactory {
    public static PuzzleListDTO createPuzzleListDTO(Puzzle puzzle) {
        PuzzleListDTO dto = new PuzzleListDTO();
        dto.setPuzzleID(puzzle.getID());
        dto.setPuzzleName(puzzle.getName());

        return dto;
    }
}
