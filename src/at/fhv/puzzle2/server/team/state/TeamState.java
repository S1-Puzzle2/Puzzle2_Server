package at.fhv.puzzle2.server.team.state;

import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.server.team.Team;

import java.util.List;

public abstract class TeamState {
    protected Team _team;

    protected TeamState(Team team) {
        _team = team;
    }

    public abstract TeamState handleCommand(Command command);

    public abstract List<Class> getAllowedCommands();
}
