package org.minijax.websocket;

import static javax.ws.rs.HttpMethod.*;

import static org.junit.Assert.*;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;

import org.junit.Ignore;
import org.junit.Test;
import org.minijax.Minijax;
import org.minijax.MinijaxApplication;
import org.minijax.MinijaxRequestContext;
import org.minijax.test.MinijaxTestRequestContext;

import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
import io.undertow.websockets.jsr.annotated.AnnotatedEndpoint;

public class WebSocketTest {

    @ServerEndpoint("/echo")
    public static class WebSocketResource {
        public static WebSocketResource lastInstance;
        public boolean opened;
        public boolean message;
        public boolean closed;

        @OnMessage
        public void onMessage(final String message, final Session session) {
        }

        @OnOpen
        public void onOpen(final Session session) {
        }

        @OnClose
        public void onClose(final Session session) {
        }

        @OnError
        public void onError(final Session session, final Throwable t) {
        }
    }


    @ServerEndpoint("/exception")
    static class ExceptionWebSocket {
        ExceptionWebSocket() {
            throw new RuntimeException();
        }
    }


    @Test
    @Ignore("Need websocket test server")
    public void testNoWebSockets() {
        final Minijax minijax = createMinijax();
        minijax.start();
    }


    @Test
    @Ignore("Need websocket test server")
    public void testRun() throws Exception {
        final Minijax minijax = createMinijax();
        minijax.register(WebSocketResource.class);
        minijax.start();

        final MinijaxApplication application = minijax.getDefaultApplication();

        final DeploymentInfo deploymentInfo = new DeploymentInfo();

        MinijaxWebSocketUtils.init(deploymentInfo, application);

        final WebSocketDeploymentInfo webSocketDeploymentInfo = (WebSocketDeploymentInfo) deploymentInfo.getServletContextAttributes().get(WebSocketDeploymentInfo.ATTRIBUTE_NAME);

        final ServerEndpointConfig endpointConfig = webSocketDeploymentInfo.getProgramaticEndpoints().get(0);

        final MinijaxWebSocketConfigurator configurator = (MinijaxWebSocketConfigurator) endpointConfig.getConfigurator();

        try (MinijaxRequestContext context = new MinijaxTestRequestContext(application, GET, "/echo")) {
            configurator.modifyHandshake(endpointConfig, null, null);

            final AnnotatedEndpoint endpoint = configurator.getEndpointInstance(AnnotatedEndpoint.class);
            assertNotNull(endpoint);
        }
    }


    private Minijax createMinijax() {
        return new Minijax() {
//            @Override
//            protected Undertow.Builder createServer() {
//                return Undertow.builder();
//            }
        };
    }
}
