package org.minijax.undertow;

import java.util.List;
import java.util.Map.Entry;

import jakarta.websocket.server.ServerEndpoint;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.minijax.Minijax;
import org.minijax.rs.MinijaxApplicationContext;
import org.minijax.rs.MinijaxRequestContext;
import org.minijax.rs.MinijaxServer;
import org.minijax.rs.util.EntityUtils;
import org.minijax.undertow.websocket.MinijaxUndertowWebSocketConnectionCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;

public class MinijaxUndertowServer implements MinijaxServer, HttpHandler {
    private static final Logger LOG = LoggerFactory.getLogger(MinijaxUndertowServer.class);
    private final Minijax minijax;
    private final Undertow undertow;

    public MinijaxUndertowServer(final Minijax minijax) {
        this(minijax, Undertow.builder());
    }

    MinijaxUndertowServer(final Minijax minijax, final Undertow.Builder undertowBuilder) {
        this.minijax = minijax;
        undertow = undertowBuilder
                .addHttpListener(minijax.getPort(), minijax.getHost())
                .setHandler(buildHandler())
                .build();
    }

    @Override
    public void start() {
        LOG.info("Minijax starting on port {}", minijax.getPort());
        undertow.start();
    }

    @Override
    public void stop() {
        undertow.stop();
    }

    @Override
    public void handleRequest(final HttpServerExchange exchange) throws Exception {
        final MinijaxApplicationContext application = minijax.getDefaultApplication();

        try (final MinijaxRequestContext ctx = new MinijaxUndertowRequestContext(application, exchange)) {
            final Response response = application.handle(ctx);

            exchange.setStatusCode(response.getStatus());

            for (final Entry<String, List<Object>> entry : response.getHeaders().entrySet()) {
                final String name = entry.getKey();
                for (final Object value : entry.getValue()) {
                    HttpString headerString = Headers.fromCache(name);
                    if (headerString == null) {
                        headerString = HttpString.tryFromString(name);
                    }
                    exchange.getResponseHeaders().add(headerString, value.toString());
                }
            }

            final MediaType mediaType = response.getMediaType();
            if (mediaType != null) {
                exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, mediaType.toString());
            }

            EntityUtils.writeEntity(response.getEntity(), mediaType, application, exchange.getOutputStream());

        } catch (final Exception ex) { // NOSONAR
            LOG.error("Unhandled exception: {}", ex.getMessage(), ex);
            throw ex; // NOSONAR
        }
    }

    private HttpHandler buildHandler() {
        final PathHandler result = Handlers.path();

        final MinijaxApplicationContext application = minijax.getDefaultApplication();
        for (final Class<?> webSocketClass : application.getWebSockets()) {
            final ServerEndpoint serverEndpoint = webSocketClass.getAnnotation(ServerEndpoint.class);
            result.addPrefixPath(serverEndpoint.value(), Handlers.websocket(new MinijaxUndertowWebSocketConnectionCallback(application, webSocketClass)));
        }

        result.addPrefixPath("/", new BlockingHandler(this));
        return result;
    }
}
