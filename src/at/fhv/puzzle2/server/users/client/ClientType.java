package at.fhv.puzzle2.server.users.client;

import java.util.Objects;

public enum  ClientType {
    Mobile("Mobile"), Unity("Unity"), Configurator("Configurator");

    private final String _clientType;
    ClientType(String type) {
        _clientType = type;
    }

    boolean isClientType(String type) {
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
