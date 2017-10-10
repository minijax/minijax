package org.minijax.websocket;

import static org.junit.Assert.*;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.eclipse.jetty.server.Server;
import org.junit.Test;
import org.minijax.Minijax;

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


    @Test
    public void testRun() {
        final MockServer server = new MockServer();

        final Minijax minijax = new Minijax() {
            @Override
            protected Server createServer(final int port) {
                return server;
            }
        };

        minijax.register(WebSocketResource.class);
        minijax.run(8080);

        assertTrue(server.started);
        assertTrue(server.joined);
    }
}
