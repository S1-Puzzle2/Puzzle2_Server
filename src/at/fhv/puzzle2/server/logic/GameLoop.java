package at.fhv.puzzle2.server.logic;

import at.fhv.puzzle2.server.DisconnectedConnectionsQueue;
import at.fhv.puzzle2.server.ReceivedCommandQueue;
import at.fhv.puzzle2.server.SendQueue;
import at.fhv.puzzle2.server.client.ClientManager;

import java.util.Date;

public class GameLoop implements Runnable {
    private final int sleepTime = 500;

    private ReceivedCommandQueue _commandQueue;
    private DisconnectedConnectionsQueue _disconnectedQueue;

    private SendQueue _sendQueue;

    private Game _game;
    private Thread _localThread = null;
    private volatile boolean _isRunning = false;

    public GameLoop(ReceivedCommandQueue commandQueue, DisconnectedConnectionsQueue disconnectedQueue) {
        _commandQueue = commandQueue;
        _disconnectedQueue = disconnectedQueue;
        _sendQueue = SendQueue.getInstance();

        _isRunning = true;

        _game = new Game(new ClientManager());

        _localThread = new Thread(this);
        _localThread.start();
    }
    @Override
    public void run() {
        while(_isRunning) {

            try {
                long startTime = new Date().getTime();

                while(_disconnectedQueue.hasNext()) {
                    _game.processDisconnectedConnection(_disconnectedQueue.next());
                }

                while(_commandQueue.hasNext()) {
                    _game.processCommand(_commandQueue.next());
                }


                long diffTime = new Date().getTime() - startTime;
                if(sleepTime - diffTime > 0) {
                    Thread.sleep(sleepTime - diffTime);
                    _sendQueue.tick(sleepTime);
                } else {
                    _sendQueue.tick(sleepTime + diffTime);
                }

            } catch (InterruptedException e) {
                //do nothing here
            }
        }
    }

    public void shutdownServer() {
        _isRunning = false;

        _localThread.interrupt();
    }
}
