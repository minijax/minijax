package org.minijax.undertow;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.Test;
import org.minijax.Minijax;
import org.mockito.Mockito;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.Headers;
import io.undertow.util.Methods;

public class HelloWorldTest {

    @Path("/")
    public static class HelloResource {

        @GET
        public static Response hello() {
            return Response.ok("Hello world!", MediaType.TEXT_PLAIN)
                    .header("X-foo", "bar")
                    .build();
        }
    }

    @Test
    public void testHello() throws Exception {
        final Minijax minijax = new Minijax().register(HelloResource.class);

        final MinijaxUndertowServer server = new MinijaxUndertowServer(minijax);

        final HeaderMap requestHeaders = new HeaderMap();
        final HeaderMap responseHeaders = new HeaderMap();
        final OutputStream outputStream = new ByteArrayOutputStream();

        final HttpServerExchange exchange = Mockito.mock(HttpServerExchange.class);
        when(exchange.getRequestURL()).thenReturn("/");
        when(exchange.getRequestMethod()).thenReturn(Methods.GET);
        when(exchange.getRequestHeaders()).thenReturn(requestHeaders);
        when(exchange.getResponseHeaders()).thenReturn(responseHeaders);
        when(exchange.getOutputStream()).thenReturn(outputStream);

        try (final MinijaxUndertowRequestContext ctx = new MinijaxUndertowRequestContext(minijax.getDefaultApplication(), exchange)) {
            server.handleRequest(exchange);
        }

        verify(exchange, times(1)).setStatusCode(eq(200));
        assertEquals("text/plain", responseHeaders.get(Headers.CONTENT_TYPE).getFirst());
        assertEquals("Hello world!", outputStream.toString());
    }
}
