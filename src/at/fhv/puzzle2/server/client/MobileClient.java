package at.fhv.puzzle2.server.client;


import at.fhv.puzzle2.communication.ClientID;
import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.server.client.state.AnswerQuestionState;
import at.fhv.puzzle2.server.client.state.ClientState;
import at.fhv.puzzle2.server.client.state.SearchPartClientState;
import at.fhv.puzzle2.server.entity.Question;
import at.fhv.puzzle2.server.logic.manager.QuestionManager;

public class MobileClient extends Client {
    private QuestionManager _questionManager;

    public MobileClient(CommandConnection connection, ClientID clientID) {
        super(connection, clientID);
    }

    @Override
    public void processCommand(Command command) {
        ClientState state = _currentState.handleCommand(command);

        if(state instanceof AnswerQuestionState) {
            ((AnswerQuestionState)state).setQuestion(_questionManager.getNextRandomQuestion());
        } else if(state instanceof SearchPartClientState) {

        }
    }
}
