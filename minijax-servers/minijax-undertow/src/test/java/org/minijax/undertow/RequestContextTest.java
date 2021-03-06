package org.minijax.undertow;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import jakarta.ws.rs.core.UriInfo;

import org.junit.jupiter.api.Test;
import org.minijax.Minijax;
import org.mockito.Mockito;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.Headers;
import io.undertow.util.Methods;

class RequestContextTest {

    @Test
    void testHttpHeaders() throws Exception {
        final Minijax minijax = new Minijax();

        final HeaderMap headerMap = new HeaderMap();
        headerMap.add(Headers.CONTENT_TYPE, "text/plain");

        final HttpServerExchange exchange = Mockito.mock(HttpServerExchange.class);
        when(exchange.getRequestMethod()).thenReturn(Methods.GET);
        when(exchange.getRequestURL()).thenReturn("http://www.example.com/");
        when(exchange.getRequestHeaders()).thenReturn(headerMap);

        try (final MinijaxUndertowRequestContext ctx = new MinijaxUndertowRequestContext(minijax.getDefaultApplication(), exchange)) {
            final MinijaxUndertowHttpHeaders httpHeaders = ctx.getHttpHeaders();
            assertEquals("text/plain", httpHeaders.getHeaderString("Content-Type"));
            assertEquals(httpHeaders, ctx.getHttpHeaders());
        }
    }

    @Test
    void testEntityStream() throws Exception {
        final Minijax minijax = new Minijax();

        final InputStream entityStream = new ByteArrayInputStream("Hello world".getBytes());

        final HeaderMap headerMap = new HeaderMap();
        headerMap.add(Headers.CONTENT_TYPE, "text/plain");

        final HttpServerExchange exchange = Mockito.mock(HttpServerExchange.class);
        when(exchange.getRequestMethod()).thenReturn(Methods.POST);
        when(exchange.getRequestURL()).thenReturn("http://www.example.com/");
        when(exchange.getRequestHeaders()).thenReturn(headerMap);
        when(exchange.getInputStream()).thenReturn(entityStream);

        try (final MinijaxUndertowRequestContext ctx = new MinijaxUndertowRequestContext(minijax.getDefaultApplication(), exchange)) {
            final InputStream entityStreamCheck = ctx.getEntityStream();
            assertNotNull(entityStreamCheck);
            assertEquals(entityStream, entityStreamCheck);
            assertEquals(entityStream, ctx.getEntityStream());
        }
    }

    @Test
    void testForwardedHttpsProto() throws Exception {
        final Minijax minijax = new Minijax();

        final HeaderMap headerMap = new HeaderMap();
        headerMap.add(Headers.X_FORWARDED_PROTO, "https");

        final HttpServerExchange exchange = Mockito.mock(HttpServerExchange.class);
        when(exchange.getRequestMethod()).thenReturn(Methods.GET);
        when(exchange.getRequestURL()).thenReturn("http://www.example.com/");
        when(exchange.getRequestHeaders()).thenReturn(headerMap);

        try (final MinijaxUndertowRequestContext ctx = new MinijaxUndertowRequestContext(minijax.getDefaultApplication(), exchange)) {
            final UriInfo uriInfo = ctx.getUriInfo();
            assertNotNull(uriInfo);
            assertEquals("https://www.example.com/", uriInfo.getRequestUri().toString());
        }
    }
}
