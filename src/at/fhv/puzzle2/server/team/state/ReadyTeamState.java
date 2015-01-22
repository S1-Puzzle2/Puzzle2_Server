package at.fhv.puzzle2.server.team.state;

import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.server.team.Team;

import java.util.List;

public class ReadyTeamState extends TeamState {
    protected ReadyTeamState(Team team) {
        super(team);
    }

    @Override
    public TeamState handleCommand(Command command) {
        return handleCommand(null);
    }

    @Override
    public List<Class> getAllowedCommands() {
        return null;
    }
}
