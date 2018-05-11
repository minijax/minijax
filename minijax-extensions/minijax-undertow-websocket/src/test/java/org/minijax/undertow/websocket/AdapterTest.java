package org.minijax.undertow.websocket;

import static java.util.Collections.*;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;

import org.junit.Test;
import org.minijax.MinijaxRequestContext;
import org.minijax.test.MinijaxTest;

public class AdapterTest extends MinijaxTest {

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
    public void testOnOpen() throws IOException {
        try (final MinijaxRequestContext ctx = createRequestContext()) {
            final MinijaxUndertowWebSocketAdapter adapter = new MinijaxUndertowWebSocketAdapter(TestSocket1.class);
            adapter.onOpen(emptyMap());
            adapter.onMessage(emptyMap());
            adapter.onClose(emptyMap());
            adapter.onError(emptyMap());
        }
    }

    @Test
    public void testOnMessage() throws IOException {
        try (final MinijaxRequestContext ctx = createRequestContext()) {
            final MinijaxUndertowWebSocketAdapter adapter = new MinijaxUndertowWebSocketAdapter(TestSocket2.class);
            adapter.onOpen(emptyMap());
            adapter.onMessage(singletonMap(String.class, "test"));
            adapter.onClose(emptyMap());
            adapter.onError(emptyMap());
        }
    }

    @Test
    public void testOnClose() throws IOException {
        try (final MinijaxRequestContext ctx = createRequestContext()) {
            final MinijaxUndertowWebSocketAdapter adapter = new MinijaxUndertowWebSocketAdapter(TestSocket3.class);
            adapter.onOpen(emptyMap());
            adapter.onMessage(emptyMap());
            adapter.onClose(emptyMap());
            adapter.onError(emptyMap());
        }
    }

    @Test
    public void testOnError() throws IOException {
        try (final MinijaxRequestContext ctx = createRequestContext()) {
            final MinijaxUndertowWebSocketAdapter adapter = new MinijaxUndertowWebSocketAdapter(TestSocket4.class);
            adapter.onOpen(emptyMap());
            adapter.onMessage(emptyMap());
            adapter.onClose(emptyMap());
            adapter.onError(emptyMap());
        }
    }
}