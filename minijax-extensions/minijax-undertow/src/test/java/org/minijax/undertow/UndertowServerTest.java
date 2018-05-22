package org.minijax.undertow;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.minijax.Minijax;
import org.mockito.Mockito;

import io.undertow.Undertow;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.Methods;

public class UndertowServerTest {

    @Test
    public void testSanity() throws Exception {
        final Minijax minijax = new Minijax();

        final MinijaxUndertowServer server = new MinijaxUndertowServer(minijax);

        final HttpServerExchange exchange = Mockito.mock(HttpServerExchange.class);
        when(exchange.getRequestMethod()).thenReturn(Methods.GET);
        when(exchange.getRequestURL()).thenReturn("https://www.example.com/");
        when(exchange.getRequestHeaders()).thenReturn(new HeaderMap());

        server.handleRequest(exchange);
    }


    @Test
    public void testStartStop() throws Exception {
        final Minijax minijax = new Minijax();

        final Undertow undertow = mock(Undertow.class);

        final Undertow.Builder undertowBuilder = mock(Undertow.Builder.class);
        when(undertowBuilder.addHttpListener(anyInt(), anyString())).thenReturn(undertowBuilder);
        when(undertowBuilder.setHandler(any())).thenReturn(undertowBuilder);
        when(undertowBuilder.build()).thenReturn(undertow);

        final MinijaxUndertowServer server = new MinijaxUndertowServer(minijax, undertowBuilder);

        server.start();
        verify(undertow, times(1)).start();

        server.stop();
        verify(undertow, times(1)).stop();
    }
}
