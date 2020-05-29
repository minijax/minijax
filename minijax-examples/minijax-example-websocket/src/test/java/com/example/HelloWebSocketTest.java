package com.example;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import jakarta.websocket.RemoteEndpoint.Basic;
import jakarta.websocket.Session;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.example.HelloWebSocket.EchoEndpoint;

@RunWith(MockitoJUnitRunner.class)
public class HelloWebSocketTest {

    @Test
    public void testWebSocket() throws IOException {
        final EchoEndpoint endpoint = new EchoEndpoint();
        final Session session = getSession();
        endpoint.onOpen(session);
        assertEquals("hello", endpoint.onMessage("hello", session));
        endpoint.onClose(session);
    }


    @Test
    public void testWebSocketError() throws IOException {
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
