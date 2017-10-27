package org.minijax.websocket;

import javax.websocket.server.ServerEndpointConfig.Configurator;

import org.minijax.Minijax;

public class MinijaxWebSocketConfigurator extends Configurator {
    private final Minijax parent;

    public MinijaxWebSocketConfigurator(final Minijax parent) {
        this.parent = parent;
    }

    @Override
    public <T> T getEndpointInstance(final Class<T> endpointClass) throws InstantiationException {
        try {
            return parent.get(endpointClass);
        } catch (final Exception ex) {
            throw new InstantiationException(ex.getMessage());
        }
    }
}
