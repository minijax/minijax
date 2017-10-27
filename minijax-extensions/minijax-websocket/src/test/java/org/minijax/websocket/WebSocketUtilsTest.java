package org.minijax.websocket;

import org.junit.Test;

public class WebSocketUtilsTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testCtor() {
        new MinijaxWebSocketUtils();
    }
}
