package org.minijax.websocket;

import javax.websocket.server.ServerEndpointConfig.Configurator;

import org.minijax.Minijax;
import org.minijax.MinijaxRequestContext;

public class MinijaxWebSocketConfigurator extends Configurator {
    private final Minijax parent;

    public MinijaxWebSocketConfigurator(final Minijax parent) {
        this.parent = parent;
    }

    @Override
    public <T> T getEndpointInstance(final Class<T> endpointClass) throws InstantiationException {
        final MinijaxRequestContext context = MinijaxRequestContext.getThreadLocal();
        if (context == null) {
            throw new InstantiationException("Missing container request context");
        }
        try {
            return parent.get(endpointClass, context, null);
        } catch (final Exception ex) {
            throw new InstantiationException(ex.getMessage());
        }
    }
}
