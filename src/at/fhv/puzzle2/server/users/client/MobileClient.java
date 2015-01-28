package at.fhv.puzzle2.server.users.client;


import at.fhv.puzzle2.communication.ClientID;
import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.server.entity.Question;
import at.fhv.puzzle2.server.users.client.state.AnswerQuestionState;
import at.fhv.puzzle2.server.users.client.state.ClientState;
import at.fhv.puzzle2.server.users.client.state.PuzzleFinishedClientState;
import at.fhv.puzzle2.server.users.client.state.SearchPartClientState;

public class MobileClient extends Client {
    private MobileClient(CommandConnection connection, ClientID clientID) {
        super(connection, clientID);
    }


    public MobileClient(CommandConnection connection) {
        this(connection, null);
    }

    @Override
    public void processCommand(Command command) {
        ClientState state = _currentState.handleCommand(command);
        if(state != null) {
            if(_currentState instanceof AnswerQuestionState && state instanceof SearchPartClientState) {
                //Notify the unity client, that a part has been unlocked
                _team.partUnlocked(((AnswerQuestionState) _currentState).getPuzzlePart());
            }

            state = fillDataInState(state);

            swapClientState(state);
        }
    }

    @Override
    public ClientState fillDataInState(ClientState state) {
        if(state instanceof AnswerQuestionState) {
            Question question = _team.getQuestionManager().getNextRandomQuestion();

            ((AnswerQuestionState) state).setQuestion(question);

        } else if(state instanceof SearchPartClientState) {
            if(!_team.getPuzzleManager().partsAvailable()) {
                return new PuzzleFinishedClientState(this);
            } else {
                SearchPartClientState searchPartClientState = (SearchPartClientState) state;
                searchPartClientState.setPuzzlePart(_team.getPuzzleManager().getNextRandomPuzzlePart());
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
