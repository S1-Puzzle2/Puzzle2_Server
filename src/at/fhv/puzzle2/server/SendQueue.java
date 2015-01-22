package at.fhv.puzzle2.server;

import at.fhv.puzzle2.communication.application.command.Command;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SendQueue {
    private List<QueuedCommand> _commandQueue;

    public SendQueue() {
        _commandQueue = Collections.synchronizedList(new LinkedList<QueuedCommand>());
    }

    public void tick(long timeElapsed) {
        for(Iterator<QueuedCommand> iterator = _commandQueue.iterator(); iterator.hasNext(); ) {
            QueuedCommand queuedCommand = iterator.next();
            queuedCommand.timeElapsed(timeElapsed);

            if(queuedCommand.shouldSendNow()) {
                queuedCommand.send();

                iterator.remove();
            }
        }
    }

    public void addCommandToSend(Command command, long delay) {
        _commandQueue.add(new QueuedCommand(command, delay));
    }

    public void addCommandToSend(Command command) {
        addCommandToSend(command, 0);
    }

    private static SendQueue _instance;
    public static synchronized SendQueue getInstance() {
        if(_instance == null) {
            _instance = new SendQueue();
        }

        return _instance;
    }
}
