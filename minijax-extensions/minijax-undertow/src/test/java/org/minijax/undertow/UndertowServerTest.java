package org.minijax.undertow;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.minijax.Minijax;
import org.mockito.Mockito;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.HttpString;

public class UndertowServerTest {

    @Test
    public void testSanity() throws Exception {

        final Minijax minijax = new Minijax();

        final MinijaxUndertowServer server = new MinijaxUndertowServer(minijax);

        final HttpServerExchange exchange = Mockito.mock(HttpServerExchange.class);
        when(exchange.getRequestMethod()).thenReturn(new HttpString("GET"));
        when(exchange.getRequestHeaders()).thenReturn(new HeaderMap());

        server.handleRequest(exchange);
    }
}
