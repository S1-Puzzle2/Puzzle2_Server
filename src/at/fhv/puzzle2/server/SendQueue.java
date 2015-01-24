package at.fhv.puzzle2.server;

import at.fhv.puzzle2.communication.application.command.Command;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SendQueue {
    private final List<QueuedCommand> _commandQueue;

    private SendQueue() {
        _commandQueue = Collections.synchronizedList(new LinkedList<>());
    }

    public void tick(long timeElapsed) {
        for(Iterator<QueuedCommand> iterator = _commandQueue.iterator(); iterator.hasNext(); ) {
            QueuedCommand queuedCommand = iterator.next();
            queuedCommand.timeElapsed(timeElapsed);

            if(queuedCommand.shouldSendNow()) {
                iterator.remove();

                queuedCommand.send();
            }
        }
    }

    public void addCommandToSend(Command command, long delay) {
        //Add the item at the front, so we keep the order of the commands to send
        _commandQueue.add(0, new QueuedCommand(command, delay));
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
