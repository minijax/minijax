package org.minijax.websocket;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.eclipse.jetty.server.Server;
import org.junit.Test;
import org.minijax.Minijax;
import org.minijax.MinijaxRequestContext;
import org.minijax.test.MockHttpServletRequest;

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
    public static class ExceptionWebSocket {
        public ExceptionWebSocket() {
            throw new RuntimeException();
        }
    }


    @Test
    public void testNoWebSockets() {
        final Minijax minijax = createMinijax();
        minijax.run(8080);
    }


    @Test
    public void testRun() throws IOException, InstantiationException {
        final Minijax minijax = createMinijax();
        minijax.register(WebSocketResource.class);
        minijax.run(8080);

        final MinijaxWebSocketConfigurator configurator = new MinijaxWebSocketConfigurator(minijax);

        final MockHttpServletRequest request = new MockHttpServletRequest("GET", URI.create("/echo"));

        try (MinijaxRequestContext context = new MinijaxRequestContext(minijax, request, null)) {
            final WebSocketResource ws = configurator.getEndpointInstance(WebSocketResource.class);
            assertNotNull(ws);
        }
    }


    @Test(expected = InstantiationException.class)
    public void testInstantiationException() throws IOException, InstantiationException {
        final Minijax minijax = createMinijax();
        minijax.register(WebSocketResource.class);
        minijax.run(8080);

        final MinijaxWebSocketConfigurator configurator = new MinijaxWebSocketConfigurator(minijax);

        final MockHttpServletRequest request = new MockHttpServletRequest("GET", URI.create("/echo"));

        try (MinijaxRequestContext context = new MinijaxRequestContext(minijax, request, null)) {
            configurator.getEndpointInstance(ExceptionWebSocket.class);
        }
    }


    private Minijax createMinijax() {
        final MockServer server = new MockServer();

        return new Minijax() {
            @Override
            protected Server createServer() {
                return server;
            }
        };
    }
}
