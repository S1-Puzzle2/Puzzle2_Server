package at.fhv.puzzle2.server.dto_factory;

import at.fhv.puzzle2.communication.application.command.dto.AnswerDTO;
import at.fhv.puzzle2.server.entity.Answer;

public class AnswerDTOFactory {
    public static AnswerDTO createAnswer(Answer answer) {
        AnswerDTO dto = new AnswerDTO();

        dto.setId(answer.getID());
        dto.setText(answer.getText());

        return dto;
    }
}
