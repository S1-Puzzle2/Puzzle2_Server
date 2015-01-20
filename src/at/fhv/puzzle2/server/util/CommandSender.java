package at.fhv.puzzle2.server.util;

import at.fhv.puzzle2.communication.application.command.Command;
import at.fhv.puzzle2.communication.application.connection.CommandConnection;

public class CommandSender {
    public static void sendCommandInThread(final CommandConnection connection, final Command command) {
        System.out.println("Sending command " + command.getCommandType());
        Thread sendThread = new Thread(() -> connection.sendCommand(command));
        sendThread.start();
    }
}
