package at.fhv.puzzle2.server.users.client;


import at.fhv.puzzle2.communication.ClientID;
import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.server.entity.Question;
import at.fhv.puzzle2.server.users.client.state.AnswerQuestionState;
import at.fhv.puzzle2.server.users.client.state.ClientState;
import at.fhv.puzzle2.server.users.client.state.PuzzleFinishedClientState;
import at.fhv.puzzle2.server.users.client.state.SearchPartClientState;

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
        if(optionalState.isPresent()) {
            if(_currentState instanceof AnswerQuestionState && optionalState.get() instanceof SearchPartClientState) {
                //Notify the unity client, that a part has been unlocked
                _team.partUnlocked(((AnswerQuestionState) _currentState).getPuzzlePart());
            }

            ClientState state = fillDataInState(optionalState.get());

            swapClientState(state);
        }
    }

    @Override
    public ClientState fillDataInState(ClientState state) {
        if(state instanceof AnswerQuestionState) {
            Optional<Question> question = _team.getQuestionManager().getNextRandomQuestion();

            ((AnswerQuestionState) state).setQuestion(question.get());

        } else if(state instanceof SearchPartClientState) {
            if(!_team.getPuzzleManager().partsAvailable()) {
                return new PuzzleFinishedClientState(this);
            } else {
                SearchPartClientState searchPartClientState = (SearchPartClientState) state;
                searchPartClientState.setPuzzlePart(_team.getPuzzleManager().getNextRandomPuzzlePart().get());
            }
        }

        return state;
    }

    @Override
    protected ClientState getStartingState() {
        SearchPartClientState searchPartClientState = new SearchPartClientState(this);
        return this.fillDataInState(searchPartClientState);
    }
}
