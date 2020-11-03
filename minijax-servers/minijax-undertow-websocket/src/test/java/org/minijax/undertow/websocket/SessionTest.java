package org.minijax.undertow.websocket;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import jakarta.websocket.MessageHandler;
import jakarta.websocket.MessageHandler.Partial;
import jakarta.websocket.MessageHandler.Whole;

import org.junit.jupiter.api.Test;

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

    @Test
    public void testGetContainer() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.getContainer();
            }
        });
    }

    @Test
    public void testAddMessageHandlerMessageHandler() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.addMessageHandler((MessageHandler) null);
            }
        });
    }

    @Test
    public void testAddMessageHandlerClassOfTWholeOfT() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.addMessageHandler((Class<Object>) null, (Whole<Object>) null);
            }
        });
    }

    @Test
    public void testAddMessageHandlerClassOfTPartialOfT() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.addMessageHandler((Class<Object>) null, (Partial<Object>) null);
            }
        });
    }

    @Test
    public void testGetMessageHandlers() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.getMessageHandlers();
            }
        });
    }

    @Test
    public void testRemoveMessageHandler() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.removeMessageHandler(null);
            }
        });
    }

    @Test
    public void testGetProtocolVersion() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.getProtocolVersion();
            }
        });
    }

    @Test
    public void testGetNegotiatedSubprotocol() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.getNegotiatedSubprotocol();
            }
        });
    }

    @Test
    public void testGetNegotiatedExtensions() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.getNegotiatedExtensions();
            }
        });
    }

    @Test
    public void testIsSecure() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.isSecure();
            }
        });
    }

    @Test
    public void testIsOpen() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.isOpen();
            }
        });
    }

    @Test
    public void testGetMaxIdleTimeout() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.getMaxIdleTimeout();
            }
        });
    }

    @Test
    public void testSetMaxIdleTimeout() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.setMaxIdleTimeout(0);
            }
        });
    }

    @Test
    public void testSetMaxBinaryMessageBufferSize() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.setMaxBinaryMessageBufferSize(0);
            }
        });
    }

    @Test
    public void testGetMaxBinaryMessageBufferSize() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.getMaxBinaryMessageBufferSize();
            }
        });
    }

    @Test
    public void testSetMaxTextMessageBufferSize() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.setMaxTextMessageBufferSize(0);
            }
        });
    }

    @Test
    public void testGetMaxTextMessageBufferSize() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.getMaxTextMessageBufferSize();
            }
        });
    }

    @Test
    public void testGetAsyncRemote() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.getAsyncRemote();
            }
        });
    }

    @Test
    public void testCloseCloseReason() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.close(null);
            }
        });
    }

    @Test
    public void testGetRequestURI() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.getRequestURI();
            }
        });
    }

    @Test
    public void testGetRequestParameterMap() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.getRequestParameterMap();
            }
        });
    }

    @Test
    public void testGetQueryString() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.getQueryString();
            }
        });
    }

    @Test
    public void testGetPathParameters() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.getPathParameters();
            }
        });
    }

    @Test
    public void testGetUserProperties() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.getUserProperties();
            }
        });
    }

    @Test
    public void testGetUserPrincipal() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.getUserPrincipal();
            }
        });
    }

    @Test
    public void testGetOpenSessions() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.getOpenSessions();
            }
        });
    }
}
