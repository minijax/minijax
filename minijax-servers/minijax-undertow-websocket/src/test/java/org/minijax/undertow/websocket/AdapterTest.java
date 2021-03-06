package org.minijax.undertow.websocket;

import static java.util.Collections.*;

import java.io.IOException;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;

import org.junit.jupiter.api.Test;
import org.minijax.rs.MinijaxRequestContext;
import org.minijax.rs.test.MinijaxTest;

class AdapterTest extends MinijaxTest {

    public static class TestSocket1 {
        @OnOpen
        public void foo() {
        }
    }

    public static class TestSocket2 {
        @OnMessage
        public String foo(final String msg) {
            return msg;
        }
    }

    public static class TestSocket3 {
        @OnClose
        public void foo() {
        }
    }

    public static class TestSocket4 {
        @OnError
        public void foo() {
        }
    }

    @Test
    void testOnOpen() throws IOException {
        try (final MinijaxRequestContext ctx = createRequestContext()) {
            final MinijaxUndertowWebSocketAdapter adapter = new MinijaxUndertowWebSocketAdapter(ctx, TestSocket1.class);
            adapter.onOpen(emptyMap());
            adapter.onMessage(emptyMap());
            adapter.onClose(emptyMap());
            adapter.onError(emptyMap());
        }
    }

    @Test
    void testOnMessage() throws IOException {
        try (final MinijaxRequestContext ctx = createRequestContext()) {
            final MinijaxUndertowWebSocketAdapter adapter = new MinijaxUndertowWebSocketAdapter(ctx, TestSocket2.class);
            adapter.onOpen(emptyMap());
            adapter.onMessage(singletonMap(String.class, "test"));
            adapter.onClose(emptyMap());
            adapter.onError(emptyMap());
        }
    }

    @Test
    void testOnClose() throws IOException {
        try (final MinijaxRequestContext ctx = createRequestContext()) {
            final MinijaxUndertowWebSocketAdapter adapter = new MinijaxUndertowWebSocketAdapter(ctx, TestSocket3.class);
            adapter.onOpen(emptyMap());
            adapter.onMessage(emptyMap());
            adapter.onClose(emptyMap());
            adapter.onError(emptyMap());
        }
    }

    @Test
    void testOnError() throws IOException {
        try (final MinijaxRequestContext ctx = createRequestContext()) {
            final MinijaxUndertowWebSocketAdapter adapter = new MinijaxUndertowWebSocketAdapter(ctx, TestSocket4.class);
            adapter.onOpen(emptyMap());
            adapter.onMessage(emptyMap());
            adapter.onClose(emptyMap());
            adapter.onError(emptyMap());
        }
    }
}
