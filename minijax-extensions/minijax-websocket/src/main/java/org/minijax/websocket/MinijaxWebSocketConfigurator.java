package org.minijax.websocket;

import javax.websocket.server.ServerEndpointConfig.Configurator;

import org.minijax.MinijaxApplication;

class MinijaxWebSocketConfigurator extends Configurator {
    private final MinijaxApplication application;

    public MinijaxWebSocketConfigurator(final MinijaxApplication application) {
        this.application = application;
    }

    @Override
    public <T> T getEndpointInstance(final Class<T> endpointClass) throws InstantiationException {
        try {
            return application.get(endpointClass);
        } catch (final Exception ex) {
            throw new InstantiationException(ex.getMessage());
        }
    }
}
