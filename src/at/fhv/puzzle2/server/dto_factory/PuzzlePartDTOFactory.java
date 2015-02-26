package at.fhv.puzzle2.server.dto_factory;

import at.fhv.puzzle2.communication.application.command.dto.PuzzlePartDTO;
import at.fhv.puzzle2.server.entity.PuzzlePart;

public class PuzzlePartDTOFactory {
    public static PuzzlePartDTO createPuzzlePartDTO(PuzzlePart puzzlePart) {
        PuzzlePartDTO dto = new PuzzlePartDTO();

        dto.setId(puzzlePart.getID());
        dto.setBarCode(puzzlePart.getBarcode());

        return dto;
    }
}
