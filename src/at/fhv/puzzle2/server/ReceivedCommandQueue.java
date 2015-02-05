package at.fhv.puzzle2.server;

import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.observer.MessageReceivedObserver;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ReceivedCommandQueue implements MessageReceivedObserver, Iterator<Command> {
    private final BlockingQueue<Command> _commandQueue = new LinkedBlockingQueue<>();

    @Override
    public void messageReceived(Command command) {
        _commandQueue.add(command);
    }

    @Override
    public boolean hasNext() {
        return _commandQueue.size() > 0;
    }

    @Override
    public Command next() {
        return _commandQueue.poll();
    }
}
