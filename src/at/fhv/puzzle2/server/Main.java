package at.fhv.puzzle2.server;

import at.fhv.puzzle2.communication.CommunicationManager;
import at.fhv.puzzle2.communication.application.command.AbstractCommand;
import at.fhv.puzzle2.communication.application.command.commands.RegisterCommand;
import at.fhv.puzzle2.communication.application.command.commands.RegisteredCommand;
import at.fhv.puzzle2.communication.connection.protocoll.ethernet.tcp.TCPEndpoint;
import at.fhv.puzzle2.communication.observable.CommandReceivedObservable;
import at.fhv.puzzle2.communication.observable.ConnectionObservable;
import at.fhv.puzzle2.communication.observer.MessageReceivedObserver;
import at.fhv.puzzle2.communication.observer.NewConnectionObserver;

import java.io.IOException;
import java.util.List;

public class Main implements MessageReceivedObserver, NewConnectionObserver {
    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        try {
            CommunicationManager cm = new CommunicationManager("PUZZLE2");
            cm.addConnectionListener(new TCPEndpoint("127.0.0.1", 4711));

            cm.addNewConnectionObserver(this);
            cm.addMessageReceivedObserver(this);
            cm.startListeningForConnections();

            System.out.println("Press any key to exit...");
            System.in.read();

            cm.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void messageReceived(CommandReceivedObservable commandReceivedObservable) {
        System.out.println("Command received!!!!!!");

        List<AbstractCommand> commandList = commandReceivedObservable.getMessageList();

        commandList.stream().filter(command -> command instanceof RegisterCommand).forEach(command -> {
            RegisteredCommand rc = new RegisteredCommand(command.getClientID());
            rc.setRegistered(true);

            System.out.println("Send registered command :D");
            command.getSender().sendCommand(rc);
        });
    }

    @Override
    public void notify(ConnectionObservable observable) {
        System.out.println("New connection!!!!");
    }
}
