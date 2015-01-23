package at.fhv.puzzle2.server.users.client;


import at.fhv.puzzle2.communication.ClientID;
import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.server.logic.manager.QuestionManager;
import at.fhv.puzzle2.server.users.client.state.AnswerQuestionState;
import at.fhv.puzzle2.server.users.client.state.ClientState;
import at.fhv.puzzle2.server.users.client.state.SearchPartClientState;

public class MobileClient extends Client {
    private QuestionManager _questionManager;

    public MobileClient(CommandConnection connection, ClientID clientID) {
        super(ClientType.Mobile, connection, clientID);
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
