package org.minijax.undertow;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map.Entry;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.minijax.Minijax;
import org.minijax.MinijaxApplication;
import org.minijax.MinijaxRequestContext;
import org.minijax.MinijaxServer;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.util.Headers;

public class MinijaxUndertowServer implements MinijaxServer, HttpHandler {
    private final Minijax minijax;
    private final Undertow undertow;

    public MinijaxUndertowServer(final Minijax minijax) {
        this.minijax = minijax;
        undertow = Undertow.builder()
                .addHttpListener(8080, "localhost")
//                .setHandler(new HttpHandler() {
//                    @Override
//                    public void handleRequest(final HttpServerExchange exchange) throws Exception {
//                        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
//                        exchange.getResponseSender().send("Hello World");
//                    }
//                })

                //.setHandler(this)
                .setHandler(new BlockingHandler(this))
                .build();
    }

    @Override
    public void start() {
        undertow.start();
    }

    @Override
    public void stop() {
        undertow.stop();
    }

    @Override
    public void handleRequest(final HttpServerExchange exchange) throws Exception {
        //exchange.startBlocking();

        final MinijaxApplication application = minijax.getDefaultApplication();

        try (final MinijaxRequestContext ctx = new MinijaxUndertowRequestContext(application, exchange)) {
            final Response response = application.handle(ctx);

            exchange.setStatusCode(response.getStatus());

            for (final Entry<String, List<Object>> entry : response.getHeaders().entrySet()) {
                final String name = entry.getKey();
                for (final Object value : entry.getValue()) {
                    exchange.getResponseHeaders().add(Headers.fromCache(name), value.toString());
                }
            }

            final MediaType mediaType = response.getMediaType();
            if (mediaType != null) {
                exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, mediaType.toString());
            }

            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            application.writeEntity(response.getEntity(), mediaType, outputStream);

            final ByteBuffer byteBuffer = ByteBuffer.wrap(outputStream.toByteArray());
            exchange.getResponseSender().send(byteBuffer);
        }
    }



//    public void start() {
//        final DeploymentInfo deploymentInfo = Servlets.deployment()
//                .setContextPath("/")
//                .setDeploymentName("Minijax")
//                .setClassLoader(Minijax.class.getClassLoader());
//
//        try {
//            for (final MinijaxApplication application : applications) {
//                addApplication(deploymentInfo, application);
//            }
//
//            final DeploymentManager deploymentManager = Servlets.defaultContainer().addDeployment(deploymentInfo);
//            deploymentManager.deploy();
//
//            server = createServer()
//                    .setHandler(deploymentManager.start())
//                    .build();
//
//            server.start();
//
//        } catch (final Exception ex) {
//            throw new MinijaxException(ex);
//        }
//    }
//
//
//    public void stop() {
//        server.stop();
//    }
//
//
//    private void addApplication(final DeploymentInfo deploymentInfo, final MinijaxApplication application)
//            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
//
//        // (1) Add Minijax filter (must come before websocket!)
//        deploymentInfo.addFilter(new FilterInfo(
//                "MinijaxFilter",
//                MinijaxFilter.class,
//                new ImmediateInstanceFactory<>(new MinijaxFilter(application))));
//        deploymentInfo.addFilterUrlMapping("MinijaxFilter", "/*", DispatcherType.REQUEST);
//
//        // (2) WebSocket endpoints
//        if (OptionalClasses.WEB_SOCKET_UTILS != null) {
//            OptionalClasses.WEB_SOCKET_UTILS
//                    .getMethod("init", DeploymentInfo.class, MinijaxApplication.class)
//                    .invoke(null, deploymentInfo, application);
//        }
//
//        // (3) Dynamic JAX-RS content
//        deploymentInfo.addServlet(new ServletInfo(
//                "MinijaxServlet",
//                MinijaxServlet.class,
//                new ImmediateInstanceFactory<>(new MinijaxServlet(application)))
//                        .setMultipartConfig(new MultipartConfigElement(""))
//                        .addMapping("/*"));
//    }
//
//
//    /**
//     * Creates a new web server listening on the given port.
//     *
//     * Override this method in unit tests to mock server functionality.
//     *
//     * @return A new web server.
//     */
//    protected Undertow.Builder createServer() throws IOException, GeneralSecurityException {
//        final Undertow.Builder builder = Undertow.builder();
//        final Map<String, Object> configuration = defaultApplication.getProperties();
//        final String host = (String) configuration.getOrDefault(MinijaxProperties.HOST, DEFAULT_HOST);
//        final int port = Integer.parseInt((String) configuration.getOrDefault(MinijaxProperties.PORT, DEFAULT_PORT));
//
//        final SSLContext sslContext = getSslContext();
//        if (sslContext != null) {
//            builder.addHttpsListener(port, host, sslContext);
//        } else {
//            builder.addHttpListener(port, host);
//        }
//
//        // In HTTP/1.1, connections are persistent unless declared
//        // otherwise.  Adding a "Connection: keep-alive" header to every
//        // response would only add useless bytes.
//        builder.setServerOption(UndertowOptions.ALWAYS_SET_KEEP_ALIVE, false);
//
//        return builder;
//    }
//
//
//    /**
//     * Creates a server connector.
//     *
//     * If an HTTPS key store is configured, returns a SSL connector for HTTPS.
//     *
//     * Otherwise, returns a normal HTTP connector by default.
//     *
//     * @param server The server.
//     * @return The server connector.
//     */
//    @SuppressWarnings("squid:S2095")
//    SSLContext getSslContext() throws IOException, GeneralSecurityException {
//        final Map<String, Object> configuration = defaultApplication.getProperties();
//        final String keyStorePath = (String) configuration.get(MinijaxProperties.SSL_KEY_STORE_PATH);
//        if (keyStorePath == null || keyStorePath.isEmpty()) {
//            return null;
//        }
//
//        final String keyStorePassword = (String) configuration.get(MinijaxProperties.SSL_KEY_STORE_PASSWORD);
//        final String keyManagerPassword = (String) configuration.get(MinijaxProperties.SSL_KEY_MANAGER_PASSWORD);
//
//        final KeyStore keyStore;
//        try (final InputStream in = Minijax.class.getClassLoader().getResourceAsStream(keyStorePath)) {
//            keyStore = KeyStore.getInstance("JKS");
//            keyStore.load(in, keyStorePassword.toCharArray());
//        }
//
//        final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//        keyManagerFactory.init(keyStore, keyManagerPassword.toCharArray());
//
//        final KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();
//
//        final SSLContext sslContext = SSLContext.getInstance("TLS");
//        sslContext.init(keyManagers, null, null);
//        return sslContext;
//    }
}
