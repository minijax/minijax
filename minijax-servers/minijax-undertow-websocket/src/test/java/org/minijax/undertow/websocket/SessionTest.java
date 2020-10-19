package org.minijax.undertow.websocket;

import static org.junit.Assert.*;

import java.io.IOException;

import jakarta.websocket.MessageHandler;
import jakarta.websocket.MessageHandler.Partial;
import jakarta.websocket.MessageHandler.Whole;

import org.junit.Test;

public class SessionTest {

    @Test
    public void testGetId() throws IOException {
        try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
            assertNull(session.getId());
        }
    }

    @Test
    public void testGetBasicRemote() throws IOException {
        try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
            assertNull(session.getBasicRemote());
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetContainer() throws IOException {
        try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
            session.getContainer();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddMessageHandlerMessageHandler() throws IOException {
        try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
            session.addMessageHandler((MessageHandler) null);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddMessageHandlerClassOfTWholeOfT() throws IOException {
        try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
            session.addMessageHandler((Class<Object>) null, (Whole<Object>) null);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddMessageHandlerClassOfTPartialOfT() throws IOException {
        try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
            session.addMessageHandler((Class<Object>) null, (Partial<Object>) null);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMessageHandlers() throws IOException {
        try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
            session.getMessageHandlers();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveMessageHandler() throws IOException {
        try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
            session.removeMessageHandler(null);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetProtocolVersion() throws IOException {
        try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
            session.getProtocolVersion();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetNegotiatedSubprotocol() throws IOException {
        try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
            session.getNegotiatedSubprotocol();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetNegotiatedExtensions() throws IOException {
        try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
            session.getNegotiatedExtensions();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsSecure() throws IOException {
        try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
            session.isSecure();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsOpen() throws IOException {
        try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
            session.isOpen();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMaxIdleTimeout() throws IOException {
        try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
            session.getMaxIdleTimeout();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetMaxIdleTimeout() throws IOException {
        try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
            session.setMaxIdleTimeout(0);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetMaxBinaryMessageBufferSize() throws IOException {
        try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
            session.setMaxBinaryMessageBufferSize(0);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMaxBinaryMessageBufferSize() throws IOException {
        try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
            session.getMaxBinaryMessageBufferSize();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetMaxTextMessageBufferSize() throws IOException {
        try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
            session.setMaxTextMessageBufferSize(0);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMaxTextMessageBufferSize() throws IOException {
        try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
            session.getMaxTextMessageBufferSize();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetAsyncRemote() throws IOException {
        try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
            session.getAsyncRemote();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCloseCloseReason() throws IOException {
        try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
            session.close(null);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetRequestURI() throws IOException {
        try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
            session.getRequestURI();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetRequestParameterMap() throws IOException {
        try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
            session.getRequestParameterMap();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetQueryString() throws IOException {
        try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
            session.getQueryString();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetPathParameters() throws IOException {
        try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
            session.getPathParameters();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetUserProperties() throws IOException {
        try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
            session.getUserProperties();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetUserPrincipal() throws IOException {
        try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
            session.getUserPrincipal();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetOpenSessions() throws IOException {
        try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
            session.getOpenSessions();
        }
    }
}
