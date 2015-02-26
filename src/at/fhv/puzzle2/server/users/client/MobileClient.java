package at.fhv.puzzle2.server.users.client;


import at.fhv.puzzle2.communication.ClientID;
import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.server.users.client.state.*;

import java.util.Optional;

public class MobileClient extends Client {
    private MobileClient(CommandConnection connection, ClientID clientID) {
        super(connection, clientID);
    }


    public MobileClient(CommandConnection connection) {
        this(connection, null);
    }

    @Override
    public void processCommand(Command command) {
        Optional<ClientState> optionalState = _currentState.handleCommand(command);
        optionalState.ifPresent(state -> {
            if(_currentState instanceof AnswerQuestionState && state instanceof SearchPartClientState) {
                //Notify the unity client, that a part has been unlocked
                _team.partUnlocked(((AnswerQuestionState) _currentState).getPuzzlePart());
            }

            if(state instanceof AnswerQuestionState && !_team.getQuestionManager().questionsRemaining()) {
                state = new NoQuestionsLeftState(this);
            } else if(state instanceof SearchPartClientState && !_team.getPuzzleManager().partsRemaining()) {
                state = new AllPartsUnlockedClientState(this);
            }

            enterClientState(state);
        });
    }

    @Override
    protected ClientState getStartingState() {
        return new SearchPartClientState(this);
    }
}
