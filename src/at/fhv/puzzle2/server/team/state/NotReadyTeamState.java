package at.fhv.puzzle2.server.team.state;

import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.command.commands.RegisterCommand;
import at.fhv.puzzle2.server.client.Client;
import at.fhv.puzzle2.server.team.Team;

import java.util.List;

public class NotReadyTeamState extends TeamState {
    protected NotReadyTeamState(Team team) {
        super(team);
    }

    @Override
    public TeamState handleCommand(Command command) {
        Client client = _team.getClientByID(command.getClientID());

        client.processCommand(command);

        if(command instanceof RegisterCommand) {

        }

        return null;
    }

    @Override
    public List<Class> getAllowedCommands() {
        return null;
    }
}
