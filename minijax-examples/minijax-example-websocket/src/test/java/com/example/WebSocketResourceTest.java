package com.example;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.example.WebSocketExample.WebSocketResource;

@RunWith(MockitoJUnitRunner.class)
public class WebSocketResourceTest {

    @Test
    public void testWebSocket() throws IOException {
        final WebSocketResource resource = new WebSocketResource();
        final Session session = getSession();
        resource.onOpen(session);
        assertEquals("hello", resource.onMessage("hello", session));
        resource.onClose(session);
    }



    @Test
    public void testWebSocketError() throws IOException {
        final WebSocketResource resource = new WebSocketResource();
        final Session session = getSession();
        resource.onOpen(session);
        resource.onError(session, new Exception());
        resource.onClose(session);
    }


    private Session getSession() {
        final Basic basicRemote = mock(Basic.class);

        final Session session = mock(Session.class);
        when(session.getBasicRemote()).thenReturn(basicRemote);
        return session;
    }
}
