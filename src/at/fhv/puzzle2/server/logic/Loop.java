package at.fhv.puzzle2.server.logic;

import java.util.Date;

/**
 * Created by sinz on 15.01.15.
 */
public class Loop implements Runnable {
    private Thread _localThread = null;
    private volatile boolean _isRunning = false;

    public Loop() {
        _isRunning = true;

        _localThread = new Thread(this);
        _localThread.start();
    }
    @Override
    public void run() {
        long sleepTime = 100;
        while(_isRunning) {

            try {
                long startTime = new Date().getTime();

                //TODO readMessages
                //TODO increment penalty timer

                //TODO check if game finished

                long diffTime = new Date().getTime() - startTime;

                Thread.sleep(sleepTime - diffTime);
            } catch (InterruptedException e) {
                //do nothing here
            }
        }
    }

    public void shutdownServer() {
        _isRunning = false;

    }
}
