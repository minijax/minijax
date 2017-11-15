package org.minijax;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.DispatcherType;
import javax.servlet.MultipartConfigElement;
import javax.ws.rs.core.CacheControl;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.minijax.cdi.MinijaxInjector;
import org.minijax.util.OptionalClasses;

public class Minijax {
    private final MinijaxInjector injector;
    private final MinijaxApplication defaultApplication;
    private final List<MinijaxApplication> applications;
    private final List<MinijaxStaticResource> staticResources;
    private final Map<String, Object> properties;


    public Minijax() {
        injector = new MinijaxInjector(this);
        defaultApplication = new MinijaxApplication(this, "/");
        applications = new ArrayList<>();
        applications.add(defaultApplication);
        staticResources = new ArrayList<>();
        properties = new HashMap<>();
    }


    public MinijaxInjector getInjector() {
        return injector;
    }


    public Map<String, Object> getProperties() {
        return properties;
    }


    public Minijax property(final String name, final Object value) {
        properties.put(name, value);
        return this;
    }


    public Minijax properties(final Map<String, String> props) {
        properties.putAll(props);
        return this;
    }


    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Minijax properties(final Properties props) {
        properties.putAll((Map) props);
        return this;
    }


    public Minijax properties(final File file) throws IOException {
        final Properties props = new Properties();
        try (final FileReader r = new FileReader(file)) {
            props.load(r);
        }
        return properties(props);
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


    public Minijax registerPersistence() {
        injector.registerPersistence();
        return this;
    }


    public Minijax packages(final String... packageNames) {
        defaultApplication.packages(packageNames);
        return this;
    }


    public Minijax addStaticFile(final String resourceName) {
        final String pathSpec = String.format("/%s", resourceName);
        return addStaticFile(resourceName, pathSpec);
    }


    public Minijax addStaticFile(final String resourceName, final String pathSpec) {
        final String resourceUrl = Minijax.class.getClassLoader().getResource(resourceName).toExternalForm();
        return addStaticResource(resourceUrl, pathSpec);
    }


    public Minijax addStaticDirectory(final String resourceName) {
        final String resourceUrl = Minijax.class.getClassLoader().getResource(resourceName).toExternalForm();
        final String pathSpec = String.format("/%s/*", resourceName);
        return addStaticResource(resourceUrl, pathSpec);
    }


    private Minijax addStaticResource(final String resourceUrl, final String pathSpec) {
        staticResources.add(new MinijaxStaticResource(resourceUrl, pathSpec));
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


    public void run() {
        run(Integer.parseInt((String) properties.getOrDefault("org.minijax.port", "8080")));
    }


    public void run(final int port) {
        try {
            final Server server = createServer();

            final ServletContextHandler context = new ServletContextHandler();
            context.setContextPath("/");
            server.setHandler(context);

            // (1) Static resources
            for (final MinijaxStaticResource staticResource : staticResources) {
                final ServletHolder staticHolder = new ServletHolder(new DefaultServlet());
                staticHolder.setInitParameter("pathInfoOnly", "true");
                staticHolder.setInitParameter("resourceBase", staticResource.getResourceBase());
                context.addServlet(staticHolder, staticResource.getPathSpec());
            }

            for (final MinijaxApplication application : applications) {
                addApplication(context, application);
            }

            // (4) HTTP or HTTPS connector
            final ServerConnector connector = createConnector(server);
            connector.setPort(port);
            server.setConnectors(new Connector[] { connector });

            server.start();
            server.join();

        } catch (final Exception ex) {
            throw new MinijaxException(ex);
        }
    }


    private void addApplication(final ServletContextHandler context, final MinijaxApplication application)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        // (1) Add Minijax filter (must come before websocket!)
        context.addFilter(new FilterHolder(new MinijaxFilter(application)), "/*", EnumSet.of(DispatcherType.REQUEST));

        // (2) WebSocket endpoints
        if (OptionalClasses.WEB_SOCKET_UTILS != null) {
            OptionalClasses.WEB_SOCKET_UTILS
                    .getMethod("init", ServletContextHandler.class, MinijaxApplication.class)
                    .invoke(null, context, application);
        }

        // (3) Dynamic JAX-RS content
        final MinijaxServlet servlet = new MinijaxServlet(application);
        final ServletHolder servletHolder = new ServletHolder(servlet);
        servletHolder.getRegistration().setMultipartConfig(new MultipartConfigElement(""));
        context.addServlet(servletHolder, "/*");
    }


    /*
     * Private helpers
     */


    /**
     * Creates a new web server listening on the given port.
     *
     * Override this method in unit tests to mock server functionality.
     *
     * @return A new web server.
     */
    protected Server createServer() {
        return new Server();
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
    private ServerConnector createConnector(final Server server) {
        final String keyStorePath = (String) properties.get(MinijaxProperties.SSL_KEY_STORE_PATH);

        if (keyStorePath == null || keyStorePath.isEmpty()) {
            // Normal HTTP
            return new ServerConnector(server);
        }

        final String keyStorePassword = (String) properties.get(MinijaxProperties.SSL_KEY_STORE_PASSWORD);
        final String keyManagerPassword = (String) properties.get(MinijaxProperties.SSL_KEY_MANAGER_PASSWORD);

        final HttpConfiguration https = new HttpConfiguration();
        https.addCustomizer(new SecureRequestCustomizer());

        final SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath(Minijax.class.getClassLoader().getResource(keyStorePath).toExternalForm());
        sslContextFactory.setKeyStorePassword(keyStorePassword);
        sslContextFactory.setKeyManagerPassword(keyManagerPassword);

        return new ServerConnector(server,
                new SslConnectionFactory(sslContextFactory, "http/1.1"),
                new HttpConnectionFactory(https));
    }


    public <T> T get(final Class<T> c) {
        return injector.get(c);
    }


}
