package at.fhv.puzzle2.server;

import at.fhv.puzzle2.communication.application.command.Command;

public class QueuedCommand {
    private long _delay;
    private Command _command;

    public QueuedCommand(Command command, long delay) {
        _command = command;
        _delay = delay;
    }

    public void timeElapsed(long time) {
        _delay -= time;
    }

    public boolean shouldSendNow() {
        return _delay <= 0;
    }

    public void send() {
        new Thread(() -> _command.getConnection().sendCommand(_command)).start();
    }
}
