package at.fhv.puzzle2.server;

import at.fhv.puzzle2.communication.CommunicationManager;
import at.fhv.puzzle2.communication.connection.protocoll.ethernet.tcp.TCPEndpoint;
import at.fhv.puzzle2.server.logic.GameLoop;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        try {
            CommunicationManager cm = new CommunicationManager("PUZZLE2");
            cm.addConnectionListener(new TCPEndpoint("127.0.0.1", 4711));


            ReceivedCommandQueue recvCommandQueue = new ReceivedCommandQueue();
            cm.addMessageReceivedObserver(recvCommandQueue);

            cm.startListeningForConnections();
            GameLoop gameLoop = new GameLoop();


            System.out.println("Press any key to exit...");
            System.in.read();

            cm.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
