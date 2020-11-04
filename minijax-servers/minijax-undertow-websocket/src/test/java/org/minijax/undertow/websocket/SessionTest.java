package org.minijax.undertow.websocket;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import jakarta.websocket.MessageHandler;
import jakarta.websocket.MessageHandler.Partial;
import jakarta.websocket.MessageHandler.Whole;

import org.junit.jupiter.api.Test;

class SessionTest {

    @Test
    void testGetId() throws IOException {
        try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
            assertNull(session.getId());
        }
    }

    @Test
    void testGetBasicRemote() throws IOException {
        try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
            assertNull(session.getBasicRemote());
        }
    }

    @Test
    void testGetContainer() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.getContainer();
            }
        });
    }

    @Test
    void testAddMessageHandlerMessageHandler() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.addMessageHandler(null);
            }
        });
    }

    @Test
    void testAddMessageHandlerClassOfTWholeOfT() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.addMessageHandler(null, (Whole<Object>) null);
            }
        });
    }

    @Test
    void testAddMessageHandlerClassOfTPartialOfT() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.addMessageHandler(null, (Partial<Object>) null);
            }
        });
    }

    @Test
    void testGetMessageHandlers() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.getMessageHandlers();
            }
        });
    }

    @Test
    void testRemoveMessageHandler() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.removeMessageHandler(null);
            }
        });
    }

    @Test
    void testGetProtocolVersion() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.getProtocolVersion();
            }
        });
    }

    @Test
    void testGetNegotiatedSubprotocol() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.getNegotiatedSubprotocol();
            }
        });
    }

    @Test
    void testGetNegotiatedExtensions() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.getNegotiatedExtensions();
            }
        });
    }

    @Test
    void testIsSecure() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.isSecure();
            }
        });
    }

    @Test
    void testIsOpen() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.isOpen();
            }
        });
    }

    @Test
    void testGetMaxIdleTimeout() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.getMaxIdleTimeout();
            }
        });
    }

    @Test
    void testSetMaxIdleTimeout() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.setMaxIdleTimeout(0);
            }
        });
    }

    @Test
    void testSetMaxBinaryMessageBufferSize() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.setMaxBinaryMessageBufferSize(0);
            }
        });
    }

    @Test
    void testGetMaxBinaryMessageBufferSize() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.getMaxBinaryMessageBufferSize();
            }
        });
    }

    @Test
    void testSetMaxTextMessageBufferSize() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.setMaxTextMessageBufferSize(0);
            }
        });
    }

    @Test
    void testGetMaxTextMessageBufferSize() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.getMaxTextMessageBufferSize();
            }
        });
    }

    @Test
    void testGetAsyncRemote() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.getAsyncRemote();
            }
        });
    }

    @Test
    void testCloseCloseReason() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.close(null);
            }
        });
    }

    @Test
    void testGetRequestURI() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.getRequestURI();
            }
        });
    }

    @Test
    void testGetRequestParameterMap() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.getRequestParameterMap();
            }
        });
    }

    @Test
    void testGetQueryString() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.getQueryString();
            }
        });
    }

    @Test
    void testGetPathParameters() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.getPathParameters();
            }
        });
    }

    @Test
    void testGetUserProperties() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.getUserProperties();
            }
        });
    }

    @Test
    void testGetUserPrincipal() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.getUserPrincipal();
            }
        });
    }

    @Test
    void testGetOpenSessions() throws IOException {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxUndertowWebSocketSession session = new MinijaxUndertowWebSocketSession(null)) {
                session.getOpenSessions();
            }
        });
    }
}
