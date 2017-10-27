package org.minijax.websocket;

import java.util.List;

import javax.servlet.ServletException;
import javax.websocket.DeploymentException;
import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;
import org.minijax.Minijax;

public class MinijaxWebSocketUtils {

    MinijaxWebSocketUtils() {
        throw new UnsupportedOperationException();
    }


    public static void init(
            final Minijax parent,
            final ServletContextHandler context,
            final List<Class<?>> webSockets)
                    throws ServletException, DeploymentException {

        final ServerContainer container = WebSocketServerContainerInitializer.configureContext(context);
        final Configurator configurator = new MinijaxWebSocketConfigurator(parent);

        for (final Class<?> c : webSockets) {
            final ServerEndpointConfig config = ServerEndpointConfig.Builder
                    .create(c, c.getAnnotation(ServerEndpoint.class).value())
                    .configurator(configurator)
                    .build();
            container.addEndpoint(config);
        }
    }
}
