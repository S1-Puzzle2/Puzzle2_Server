package at.fhv.puzzle2.server;

import at.fhv.puzzle2.communication.ClientID;
import at.fhv.puzzle2.communication.CommunicationManager;
import at.fhv.puzzle2.communication.application.command.commands.RegisterCommand;
import at.fhv.puzzle2.communication.connection.protocoll.ethernet.tcp.TCPEndpoint;
import at.fhv.puzzle2.communication.observable.ConnectionObservable;
import at.fhv.puzzle2.communication.observer.NewConnectionObserver;
import at.fhv.puzzle2.server.logic.GameLoop;
import at.fhv.puzzle2.server.state.BeforeGameStartState;
import at.fhv.puzzle2.server.state.GameState;

import java.io.IOException;

public class Main implements NewConnectionObserver {
    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        try {
            CommunicationManager cm = new CommunicationManager("PUZZLE2");
            cm.addConnectionListener(new TCPEndpoint("127.0.0.1", 4711));


            ReceivedCommandQueue recvCommandQueue = new ReceivedCommandQueue();
            DisconnectedConnectionsQueue disconnectedConnectionsQueue = new DisconnectedConnectionsQueue();

            cm.addMessageReceivedObserver(recvCommandQueue);
            cm.addNewConnectionObserver(this);
            cm.addConnectionClosedObserver(disconnectedConnectionsQueue);

            cm.startListeningForConnections();
            GameLoop gameLoop = new GameLoop(recvCommandQueue, disconnectedConnectionsQueue);


            System.out.println("Press any key to exit...");
            System.in.read();

            cm.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notify(ConnectionObservable observable) {
        System.out.println("New connection!!!");
    }
}
