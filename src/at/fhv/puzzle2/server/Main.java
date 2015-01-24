package at.fhv.puzzle2.server;

import at.fhv.puzzle2.communication.CommunicationManager;
import at.fhv.puzzle2.communication.connection.protocoll.ethernet.tcp.TCPEndpoint;
import at.fhv.puzzle2.communication.observable.ConnectionObservable;
import at.fhv.puzzle2.communication.observer.NewConnectionObserver;
import at.fhv.puzzle2.server.database.Database;
import at.fhv.puzzle2.server.logic.GameLoop;

import java.io.IOException;
import java.sql.SQLException;

public class Main implements NewConnectionObserver {
    public static void main(String[] args) {
        new Main();
    }

    private Main() {
        try {
            //Initialize configuration and database
            Configuration.initConfiguration();
            Configuration configuration = Configuration.getInstance();
            Database.initDatabase();

            CommunicationManager cm = new CommunicationManager(configuration.getStringOrDefault("server.broadcast_message", "PUZZLE2"));
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

            gameLoop.shutdownServer();

            cm.close();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        } catch (ConfigurationException e) {
            System.out.println("Configuration-Fehler: " + e.getMessage());
        }
    }

    @Override
    public void notify(ConnectionObservable observable) {
        System.out.println("New connection!!!");
    }
}
