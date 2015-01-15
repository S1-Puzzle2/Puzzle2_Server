package at.fhv.puzzle2.server;

import at.fhv.puzzle2.communication.application.command.AbstractCommand;
import at.fhv.puzzle2.communication.observable.CommandReceivedObservable;
import at.fhv.puzzle2.communication.observer.MessageReceivedObserver;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ReceivedCommandQueue implements MessageReceivedObserver, Iterator<AbstractCommand> {
    private BlockingQueue<AbstractCommand> _commandQueue = new LinkedBlockingQueue<>();

    @Override
    public void messageReceived(CommandReceivedObservable commandReceivedObservable) {
        _commandQueue.addAll(commandReceivedObservable.getMessageList());
    }

    @Override
    public boolean hasNext() {
        return _commandQueue.size() > 0;
    }

    @Override
    public AbstractCommand next() {
        return _commandQueue.poll();
    }
}
