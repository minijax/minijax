package org.minijax.undertow.websocket;

import static jakarta.ws.rs.HttpMethod.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.ws.rs.core.HttpHeaders;

import org.junit.jupiter.api.Test;
import org.minijax.Minijax;
import org.minijax.rs.MinijaxApplicationContext;

import io.undertow.websockets.spi.WebSocketHttpExchange;

public class RequestContextTest {

    @Test
    public void testBasic() throws Exception {
        final Minijax minijax = new Minijax();
        final MinijaxApplicationContext app = minijax.getDefaultApplication();

        final Map<String, List<String>> headerMap = new HashMap<>();
        headerMap.computeIfAbsent(HttpHeaders.CONTENT_TYPE, k -> new ArrayList<>()).add("text/plain");

        final WebSocketHttpExchange exchange = mock(WebSocketHttpExchange.class);
        when(exchange.getRequestHeaders()).thenReturn(headerMap);

        try (final MinijaxUndertowWebSocketRequestContext ctx = new MinijaxUndertowWebSocketRequestContext(app, exchange)) {
            assertEquals(GET, ctx.getMethod());
            assertNotNull(ctx.getUriInfo());
            assertNotNull(ctx.getHttpHeaders());
            assertNull(ctx.getEntityStream());
        }
    }
}
