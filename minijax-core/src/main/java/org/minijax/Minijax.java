package org.minijax;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.servlet.DispatcherType;
import javax.servlet.MultipartConfigElement;
import javax.ws.rs.core.CacheControl;

import org.minijax.cdi.MinijaxInjector;
import org.minijax.util.OptionalClasses;
import org.minijax.util.UrlUtils;

import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.FilterInfo;
import io.undertow.servlet.api.ServletInfo;
import io.undertow.servlet.util.ImmediateInstanceFactory;

/**
 * The Minijax class represents a container for JAX-RS applications.
 *
 * Create and launch a JAX-RS application:
 *
 * <pre>
 * {@code
 *     {@literal @}Path("/")
 *     public class Hello {
 *
 *         {@literal @}GET
 *         public static String hello() {
 *             return "Hello world!";
 *         }
 *
 *         public static void main(String[] args) {
 *             new Minijax()
 *                     .register(Hello.class)
 *                     .start();
 *         }
 *     }
 * }
 * </pre>
 */
public class Minijax {
    @SuppressWarnings("squid:S1313")
    public static final String DEFAULT_HOST = "0.0.0.0";
    public static final String DEFAULT_PORT = "8080";
    private final MinijaxInjector injector;
    private final MinijaxConfiguration configuration;
    private final MinijaxApplication defaultApplication;
    private final List<MinijaxApplication> applications;
    private Undertow server;

    public Minijax() {
        injector = new MinijaxInjector(this);
        configuration = new MinijaxConfiguration();
        defaultApplication = new MinijaxApplication(this, "/");
        applications = new ArrayList<>();
        applications.add(defaultApplication);
    }


    public MinijaxInjector getInjector() {
        return injector;
    }


    public <T> T getResource(final Class<T> c) {
        return injector.getResource(c);
    }


    public Map<String, Object> getProperties() {
        return configuration.getProperties();
    }


    public Minijax property(final String name, final Object value) {
        configuration.property(name, value);
        return this;
    }


    public Minijax properties(final Map<String, String> props) {
        configuration.properties(props);
        return this;
    }


    public Minijax properties(final Properties props) {
        configuration.properties(props);
        return this;
    }


    public Minijax properties(final File file) throws IOException {
        configuration.properties(file);
        return this;
    }


    public Minijax properties(final InputStream inputStream) throws IOException {
        configuration.properties(inputStream);
        return this;
    }


    public Minijax properties(final String fileName) throws IOException {
        configuration.properties(fileName);
        return this;
    }


    public MinijaxApplication getDefaultApplication() {
        return defaultApplication;
    }


    public MinijaxApplication getApplication(final URI requestUri) {
        final String requestPath = requestUri.getPath();
        for (final MinijaxApplication application : applications) {
            if (requestPath.startsWith(application.getPath())) {
                return application;
            }
        }
        return null;
    }


    public Minijax register(final Class<?> componentClass) {
        defaultApplication.register(componentClass);
        return this;
    }


    public Minijax register(final Class<?> componentClass, final int priority) {
        defaultApplication.register(componentClass, priority);
        return this;
    }


    public Minijax register(final Class<?> componentClass, final Class<?>... contracts) {
        defaultApplication.register(componentClass, contracts);
        return this;
    }


    public Minijax register(final Class<?> componentClass, final Map<Class<?>, Integer> contracts) {
        defaultApplication.register(componentClass, contracts);
        return this;
    }


    public Minijax register(final Object component) {
        defaultApplication.register(component);
        return this;
    }


    public Minijax register(final Object component, final int priority) {
        defaultApplication.register(component, priority);
        return this;
    }


    public Minijax register(final Object component, final Class<?>... contracts) {
        defaultApplication.register(component, contracts);
        return this;
    }


    public Minijax register(final Object component, final Map<Class<?>, Integer> contracts) {
        defaultApplication.register(component, contracts);
        return this;
    }


    public Minijax packages(final String... packageNames) {
        defaultApplication.packages(packageNames);
        return this;
    }


    public Minijax staticFiles(final String... resourceNames) {
        for (final String resourceName : resourceNames) {
            staticFile(resourceName, resourceName);
        }
        return this;
    }


    public Minijax staticFile(final String resourceName, final String path) {
        final String pathSpec = UrlUtils.concatUrlPaths(path, null);
        defaultApplication.addResourceMethod(new MinijaxStaticResource(resourceName, pathSpec));
        return this;
    }


    public Minijax staticDirectories(final String... resourceNames) {
        for (final String resourceName : resourceNames) {
            staticDirectory(resourceName, resourceName);
        }
        return this;
    }


    public Minijax staticDirectory(final String resourceName, final String path) {
        final String pathSpec = UrlUtils.concatUrlPaths(path, "{file:.*}");
        defaultApplication.addResourceMethod(new MinijaxStaticResource(resourceName, pathSpec));
        return this;
    }


    public Minijax allowCors(final String urlPrefix) {
        defaultApplication.allowCors(urlPrefix);
        return this;
    }


    public Minijax secure(final String keyStorePath, final String keyStorePassword, final String keyManagerPassword) {
        property(MinijaxProperties.SSL_KEY_STORE_PATH, keyStorePath);
        property(MinijaxProperties.SSL_KEY_STORE_PASSWORD, keyStorePassword);
        property(MinijaxProperties.SSL_KEY_MANAGER_PASSWORD, keyManagerPassword);
        return this;
    }


    public Minijax defaultCacheControl(final CacheControl defaultCacheControl) {
        defaultApplication.register(new MinijaxCacheControlFilter(defaultCacheControl));
        return this;
    }


    public void start(final int port) {
        property(MinijaxProperties.PORT, Integer.toString(port));
        start();
    }


    public void start() {
        final DeploymentInfo deploymentInfo = Servlets.deployment()
                .setContextPath("/")
                .setDeploymentName("Minijax")
                .setClassLoader(Minijax.class.getClassLoader());

        try {
            for (final MinijaxApplication application : applications) {
                addApplication(deploymentInfo, application);
            }

            final DeploymentManager deploymentManager = Servlets.defaultContainer().addDeployment(deploymentInfo);
            deploymentManager.deploy();

            server = createServer()
                    .setHandler(deploymentManager.start())
                    .build();

            server.start();

        } catch (final Exception ex) {
            throw new MinijaxException(ex);
        }
    }


    public void stop() {
        server.stop();
    }


    private void addApplication(final DeploymentInfo deploymentInfo, final MinijaxApplication application)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        // (0) Sort the resource methods by literal length
        application.sortResourceMethods();

        // (1) Add Minijax filter (must come before websocket!)
        deploymentInfo.addFilter(new FilterInfo(
                "MinijaxFilter",
                MinijaxFilter.class,
                new ImmediateInstanceFactory<>(new MinijaxFilter(application))));
        deploymentInfo.addFilterUrlMapping("MinijaxFilter", "/*", DispatcherType.REQUEST);

        // (2) WebSocket endpoints
        if (OptionalClasses.WEB_SOCKET_UTILS != null) {
            OptionalClasses.WEB_SOCKET_UTILS
                    .getMethod("init", DeploymentInfo.class, MinijaxApplication.class)
                    .invoke(null, deploymentInfo, application);
        }

        // (3) Dynamic JAX-RS content
        deploymentInfo.addServlet(new ServletInfo(
                "MinijaxServlet",
                MinijaxServlet.class,
                new ImmediateInstanceFactory<>(new MinijaxServlet(application)))
                        .setMultipartConfig(new MultipartConfigElement(""))
                        .addMapping("/*"));
    }


    /**
     * Creates a new web server listening on the given port.
     *
     * Override this method in unit tests to mock server functionality.
     *
     * @return A new web server.
     */
    protected Undertow.Builder createServer() throws IOException, GeneralSecurityException {
        final Undertow.Builder builder = Undertow.builder();
        final String host = configuration.getOrDefault(MinijaxProperties.HOST, DEFAULT_HOST);
        final int port = Integer.parseInt(configuration.getOrDefault(MinijaxProperties.PORT, DEFAULT_PORT));

        final SSLContext sslContext = getSslContext();
        if (sslContext != null) {
            builder.addHttpsListener(port, host, sslContext);
        } else {
            builder.addHttpListener(port, host);
        }

        // In HTTP/1.1, connections are persistent unless declared
        // otherwise.  Adding a "Connection: keep-alive" header to every
        // response would only add useless bytes.
        builder.setServerOption(UndertowOptions.ALWAYS_SET_KEEP_ALIVE, false);

        return builder;
    }


    /**
     * Creates a server connector.
     *
     * If an HTTPS key store is configured, returns a SSL connector for HTTPS.
     *
     * Otherwise, returns a normal HTTP connector by default.
     *
     * @param server The server.
     * @return The server connector.
     */
    @SuppressWarnings("squid:S2095")
    SSLContext getSslContext() throws IOException, GeneralSecurityException {
        final String keyStorePath = (String) configuration.get(MinijaxProperties.SSL_KEY_STORE_PATH);
        if (keyStorePath == null || keyStorePath.isEmpty()) {
            return null;
        }

        final String keyStorePassword = (String) configuration.get(MinijaxProperties.SSL_KEY_STORE_PASSWORD);
        final String keyManagerPassword = (String) configuration.get(MinijaxProperties.SSL_KEY_MANAGER_PASSWORD);

        final KeyStore keyStore;
        try (final InputStream in = Minijax.class.getClassLoader().getResourceAsStream(keyStorePath)) {
            keyStore = KeyStore.getInstance("JKS");
            keyStore.load(in, keyStorePassword.toCharArray());
        }

        final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, keyManagerPassword.toCharArray());

        final KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();

        final SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagers, null, null);
        return sslContext;
    }
}
