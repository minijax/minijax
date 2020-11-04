package com.example;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import jakarta.websocket.RemoteEndpoint.Basic;
import jakarta.websocket.Session;

import org.junit.jupiter.api.Test;

import com.example.HelloWebSocket.EchoEndpoint;

class HelloWebSocketTest {

    @Test
    void testWebSocket() throws IOException {
        final EchoEndpoint endpoint = new EchoEndpoint();
        final Session session = getSession();
        endpoint.onOpen(session);
        assertEquals("hello", endpoint.onMessage("hello", session));
        endpoint.onClose(session);
    }


    @Test
    void testWebSocketError() throws IOException {
        final EchoEndpoint endpoint = new EchoEndpoint();
        final Session session = getSession();
        endpoint.onOpen(session);
        endpoint.onError(session, new Exception());
        endpoint.onClose(session);
    }

    private Session getSession() {
        final Basic basicRemote = mock(Basic.class);

        final Session session = mock(Session.class);
        when(session.getBasicRemote()).thenReturn(basicRemote);
        return session;
    }
}
