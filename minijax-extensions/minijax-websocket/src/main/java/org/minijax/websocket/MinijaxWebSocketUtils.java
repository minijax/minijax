package org.minijax.websocket;

import javax.servlet.ServletException;
import javax.websocket.DeploymentException;
import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;
import org.minijax.MinijaxApplication;

public class MinijaxWebSocketUtils {

    MinijaxWebSocketUtils() {
        throw new UnsupportedOperationException();
    }

    public static void init(final ServletContextHandler context, final MinijaxApplication application)
            throws ServletException, DeploymentException {

        final ServerContainer container = WebSocketServerContainerInitializer.configureContext(context);
        final Configurator configurator = new MinijaxWebSocketConfigurator(application);

        for (final Class<?> c : application.getWebSockets()) {
            final ServerEndpointConfig config = ServerEndpointConfig.Builder
                    .create(c, c.getAnnotation(ServerEndpoint.class).value())
                    .configurator(configurator)
                    .build();
            container.addEndpoint(config);
        }
    }
}
