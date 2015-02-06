package at.fhv.puzzle2.server.users.client;

import java.util.Objects;
import java.util.Optional;

public enum  ClientType {
    Mobile("Mobile"), Unity("Unity"), Configurator("Configurator");

    private final String _clientType;
    ClientType(String type) {
        _clientType = type;
    }

    boolean isClientType(String type) {
        return Objects.equals(_clientType, type);
    }

    public static Optional<ClientType> getClientTypeByString(String type) {
        if(Mobile.isClientType(type)) {
            return Optional.of(Mobile);
        }

        if(Unity.isClientType(type)) {
            return Optional.of(Unity);
        }

        if(Configurator.isClientType(type)) {
            return Optional.of(Configurator);
        }

        return null;
    }
}
