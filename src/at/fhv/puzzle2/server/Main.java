package at.fhv.puzzle2.server;

import at.fhv.puzzle2.communication.CommunicationManager;
import at.fhv.puzzle2.communication.application.connection.encryption.AESEncryption;
import at.fhv.puzzle2.communication.application.connection.encryption.Encryption;
import at.fhv.puzzle2.communication.connection.protocoll.ethernet.tcp.TCPEndpoint;
import at.fhv.puzzle2.logging.Exception.LogFormatterUnknownException;
import at.fhv.puzzle2.logging.Logger;
import at.fhv.puzzle2.server.database.Database;
import at.fhv.puzzle2.server.logic.GameLoop;

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class Main {
    private static final String TAG = "server.Main";

    public static void main(String[] args) {
        new Main();
    }

    private Main() {
        try {
            Initializer.initialize();

            Configuration configuration = Configuration.getInstance();

            //Load the encryption
            String password = configuration.getString("server.password");
            Encryption encryption = null;
            if(password != null) {
                encryption = new AESEncryption(password);
            }

            CommunicationManager cm = new CommunicationManager(configuration.getStringOrDefault("server.broadcast_message", "PUZZLE2"), encryption);
            cm.addConnectionListener(new TCPEndpoint("127.0.0.1", 4711));


            ReceivedCommandQueue recvCommandQueue = new ReceivedCommandQueue();
            DisconnectedConnectionsQueue disconnectedConnectionsQueue = new DisconnectedConnectionsQueue();

            cm.addMessageReceivedObserver(recvCommandQueue);
            cm.addConnectionClosedObserver(disconnectedConnectionsQueue);

            cm.startListeningForConnections();
            GameLoop gameLoop = new GameLoop(recvCommandQueue, disconnectedConnectionsQueue);

            Logger.getLogger().info(TAG, "Puzzle2-Server is ready...");

            System.out.println("Press any key to exit...");
            System.in.read();

            gameLoop.shutdownServer();

            cm.close();

            Logger.getLogger().info(TAG, "Server closed...");

            Database.getInstance().closeConnection();
            Logger.getLogger().close();
        } catch (ConfigurationException e) {
            System.out.println("Configuration-Fehler: " + e.getMessage());
        } catch (IOException | SQLException | LogFormatterUnknownException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }
}
