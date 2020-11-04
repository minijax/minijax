package org.minijax.undertow;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import jakarta.websocket.OnMessage;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import org.junit.jupiter.api.Test;
import org.minijax.Minijax;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;

class HelloWebSocketTest {

    @ServerEndpoint("/echo")
    public static class EchoEndpoint {
        @OnMessage
        public String onMessage(final String message, final Session session) {
            return message;
        }
    }

    @Test
    void testHello() throws Exception {
        final Minijax minijax = new Minijax().register(EchoEndpoint.class);

        final Undertow undertow = mock(Undertow.class);

        final List<HttpHandler> handlers = new ArrayList<>();

        final Undertow.Builder undertowBuilder = mock(Undertow.Builder.class);
        when(undertowBuilder.addHttpListener(anyInt(), anyString())).thenReturn(undertowBuilder);
        when(undertowBuilder.setHandler(any())).then(inv -> {
            handlers.add(inv.getArgument(0));
            return undertowBuilder;
        });
        when(undertowBuilder.build()).thenReturn(undertow);

        new MinijaxUndertowServer(minijax, undertowBuilder);
        assertEquals(1, handlers.size());

        final PathHandler pathHandler = (PathHandler) handlers.get(0);
        final PathHandler echoHandler = pathHandler.removeExactPath("/echo");
        assertNotNull(echoHandler);
    }
}
