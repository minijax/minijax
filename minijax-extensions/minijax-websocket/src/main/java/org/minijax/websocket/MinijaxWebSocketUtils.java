package org.minijax.websocket;

import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;

import org.minijax.MinijaxApplication;

import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
import io.undertow.websockets.jsr.annotated.AnnotatedEndpoint;

public class MinijaxWebSocketUtils {

    MinijaxWebSocketUtils() {
        throw new UnsupportedOperationException();
    }

    public static void init(final DeploymentInfo deploymentInfo, final MinijaxApplication application) {
        final WebSocketDeploymentInfo webSockets = new WebSocketDeploymentInfo();
        for (final Class<?> endpoint : application.getWebSockets()) {
            final ServerEndpoint serverEndpoint = endpoint.getAnnotation(ServerEndpoint.class);
            final MinijaxWebSocketConfigurator configurator = new MinijaxWebSocketConfigurator(application, endpoint);
            final ServerEndpointConfig endpointConfig = ServerEndpointConfig.Builder.create(AnnotatedEndpoint.class, serverEndpoint.value())
                    .configurator(configurator)
                    .build();

            webSockets.addEndpoint(endpointConfig);
        }

        deploymentInfo.addServletContextAttribute(WebSocketDeploymentInfo.ATTRIBUTE_NAME, webSockets);
    }
}
