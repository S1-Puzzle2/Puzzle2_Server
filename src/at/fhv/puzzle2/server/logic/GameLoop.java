package at.fhv.puzzle2.server.logic;

import at.fhv.puzzle2.server.Configuration;
import at.fhv.puzzle2.server.DisconnectedConnectionsQueue;
import at.fhv.puzzle2.server.ReceivedCommandQueue;
import at.fhv.puzzle2.server.SendQueue;
import at.fhv.puzzle2.server.game.Game;

import java.sql.SQLException;
import java.util.Date;

public class GameLoop implements Runnable {
    private int sleepTime;

    private final ReceivedCommandQueue _commandQueue;
    private final DisconnectedConnectionsQueue _disconnectedQueue;

    private final SendQueue _sendQueue;

    private Game _game;
    private Thread _localThread = null;
    private volatile boolean _isRunning = false;

    public GameLoop(ReceivedCommandQueue commandQueue, DisconnectedConnectionsQueue disconnectedQueue) throws SQLException {
        _commandQueue = commandQueue;
        _disconnectedQueue = disconnectedQueue;
        _sendQueue = SendQueue.getInstance();


        _isRunning = true;

        _game = new Game();

        initialize();


        _localThread = new Thread(this);
        _localThread.start();
    }

    private void initialize() {
        //Get the loop time from the configuration
        Configuration configuration = Configuration.getInstance();
        sleepTime = configuration.getIntegerOrDefault("server.loop_time", 500);
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
                }

                long timeElapsed = Math.max(sleepTime, diffTime);

                _sendQueue.tick(timeElapsed);
                _game.timeElapsed(timeElapsed);

                if(_game.hasStatusChanged()) {
                    _game.notifyStatusChangedListeners();

                    _game.unsetStatusChanged();
                }

                if(_game.clientHasFinished()) {
                    _game.gameFinished();
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
