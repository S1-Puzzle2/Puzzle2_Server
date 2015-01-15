package at.fhv.puzzle2.server.client;

import at.fhv.puzzle2.communication.application.command.commands.UnknownCommand;

import java.util.Objects;

public enum  ClientType {
    Mobile("Mobile"), Unity("Unity"), Configurator("Configurator");

    private String _clientType;
    ClientType(String type) {
        _clientType = type;
    }

    public boolean isClientType(String type) {
        return Objects.equals(_clientType, type);
    }

    public static ClientType getClientTypeByString(String type) {
        if(Mobile.isClientType(type)) {
            return Mobile;
        }

        if(Unity.isClientType(type)) {
            return Unity;
        }

        if(Configurator.isClientType(type)) {
            return Configurator;
        }

        return null;
    }
}
