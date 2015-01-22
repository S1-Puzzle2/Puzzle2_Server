package at.fhv.puzzle2.server;

import at.fhv.puzzle2.communication.application.connection.CommandConnection;
import at.fhv.puzzle2.communication.observable.ConnectionObservable;
import at.fhv.puzzle2.communication.observer.ClosedConnectionObserver;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DisconnectedConnectionsQueue implements ClosedConnectionObserver, Iterator<CommandConnection> {
    private BlockingQueue<CommandConnection> _closedConnectionQueue = new LinkedBlockingQueue<>();

    @Override
    public void notify(ConnectionObservable observable) {
        _closedConnectionQueue.addAll(observable.getConnectionList());
    }

    @Override
    public boolean hasNext() {
        return _closedConnectionQueue.size() > 0;
    }

    @Override
    public CommandConnection next() {
        return _closedConnectionQueue.poll();
    }
}
