package com.example;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.minijax.Minijax;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketExample {
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketExample.class);

    @ServerEndpoint("/echo")
    public static class WebSocketResource {

        @OnOpen
        public void onOpen(final Session session) {
            LOG.info("[Session {}] Session has been opened.", session.getId());
            try {
                session.getBasicRemote().sendText("Connection Established");
            } catch (final IOException ex) {
                LOG.error(ex.getMessage(), ex);
            }
        }

        @OnMessage
        public String onMessage(final String message, final Session session) {
            LOG.info("[Session {}] Sending message: {}", session.getId(), message);
            return message;
        }

        @OnClose
        public void onClose(final Session session) {
            LOG.info("[Session {}] Session has been closed.", session.getId());
        }

        @OnError
        public void onError(final Session session, final Throwable t) {
            LOG.info("[Session {}] An error has been detected: {}.", session.getId(), t.getMessage());
        }
    }

    @GET
    @Path("/")
    public static String hello() {
        return "Hello world!";
    }

    public static void main(final String[] args) {
        new Minijax()
                .register(WebSocketExample.class)
                .register(WebSocketResource.class)
                .run(8081);
    }
}
