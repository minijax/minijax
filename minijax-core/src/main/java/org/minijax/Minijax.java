package org.minijax;

import static jakarta.ws.rs.HttpMethod.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.CacheControl;

import org.apache.commons.io.IOUtils;
import org.minijax.cdi.MinijaxInjector;
import org.minijax.commons.MinijaxException;
import org.minijax.commons.MinijaxProperties;
import org.minijax.rs.MinijaxApplication;
import org.minijax.rs.MinijaxCacheControlFilter;
import org.minijax.rs.MinijaxRequestContext;
import org.minijax.rs.MinijaxServer;
import org.minijax.rs.MinijaxStaticResource;
import org.minijax.rs.test.MinijaxTestClient;
import org.minijax.rs.test.MinijaxTestRequestContext;
import org.minijax.rs.test.MinijaxTestWebTarget;
import org.minijax.rs.util.ClassPathScanner;
import org.minijax.rs.util.UrlUtils;

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
    public static final int DEFAULT_PORT = 8080;
    private final List<MinijaxApplication> applications;
    private MinijaxServer server;
    private String host;
    private int port;

    public Minijax() {
        applications = new ArrayList<>();
        host = DEFAULT_HOST;
        port = DEFAULT_PORT;
    }

    public List<MinijaxApplication> getApplications() {
        return applications;
    }

    public MinijaxApplication getDefaultApplication() {
        if (applications.isEmpty()) {
            applications.add(new MinijaxApplication());
        }
        return applications.get(0);
    }

    public MinijaxInjector getInjector() {
        return getDefaultApplication().getInjector();
    }

    public <T> T getResource(final Class<T> c) {
        return getDefaultApplication().getResource(c);
    }

    public Minijax property(final String name, final Object value) {
        getDefaultApplication().property(name, value);
        return this;
    }

    public Minijax properties(final Map<String, String> props) {
        getDefaultApplication().properties(props);
        return this;
    }

    public Minijax properties(final Properties props) {
        getDefaultApplication().properties(props);
        return this;
    }

    public Minijax properties(final File file) throws IOException {
        getDefaultApplication().properties(file);
        return this;
    }

    public Minijax properties(final InputStream inputStream) throws IOException {
        getDefaultApplication().properties(inputStream);
        return this;
    }

    public Minijax properties(final String fileName) throws IOException {
        getDefaultApplication().properties(fileName);
        return this;
    }

    public MinijaxApplication getApplication(final URI requestUri) {
        if (applications.size() == 1) {
            // Common case is only the default application
            return applications.get(0);
        }
        final String requestPath = requestUri.getPath();
        for (final MinijaxApplication application : applications) {
            if (requestPath.startsWith(application.getPath())) {
                return application;
            }
        }
        return null;
    }

    public Minijax bind(final Object instance, final Class<?> contract) {
        getDefaultApplication().bind(instance, contract);
        return this;
    }

    public Minijax bind(final Class<?> component, final Class<?> contract) {
        getDefaultApplication().bind(component, contract);
        return this;
    }

    @SuppressWarnings("unchecked")
    public Minijax register(final Class<?> componentClass) {
        if (jakarta.ws.rs.core.Application.class.isAssignableFrom(componentClass)) {
            applications.add(new MinijaxApplication((Class<Application>) componentClass));
        } else {
            getDefaultApplication().register(componentClass);
        }
        return this;
    }

    public Minijax register(final Class<?> componentClass, final int priority) {
        getDefaultApplication().register(componentClass, priority);
        return this;
    }

    public Minijax register(final Class<?> componentClass, final Class<?>... contracts) {
        getDefaultApplication().register(componentClass, contracts);
        return this;
    }

    public Minijax register(final Class<?> componentClass, final Map<Class<?>, Integer> contracts) {
        getDefaultApplication().register(componentClass, contracts);
        return this;
    }

    public Minijax register(final Object component) {
        if (component instanceof MinijaxApplication) {
            applications.add((MinijaxApplication) component);
        } else {
            getDefaultApplication().register(component);
        }
        return this;
    }

    public Minijax register(final Object component, final int priority) {
        getDefaultApplication().register(component, priority);
        return this;
    }

    public Minijax register(final Object component, final Class<?>... contracts) {
        getDefaultApplication().register(component, contracts);
        return this;
    }

    public Minijax register(final Object component, final Map<Class<?>, Integer> contracts) {
        getDefaultApplication().register(component, contracts);
        return this;
    }

    public Minijax packages(final String... packageNames) {
        for (final Class<?> c : ClassPathScanner.scan(packageNames)) {
            register(c);
        }
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
        getDefaultApplication().addResourceMethod(new MinijaxStaticResource(resourceName, pathSpec));
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
        getDefaultApplication().addResourceMethod(new MinijaxStaticResource(resourceName, pathSpec));
        return this;
    }

    public Minijax allowCors(final String urlPrefix) {
        getDefaultApplication().allowCors(urlPrefix);
        return this;
    }

    public Minijax secure(final String keyStorePath, final String keyStorePassword, final String keyManagerPassword) {
        property(MinijaxProperties.SSL_KEY_STORE_PATH, keyStorePath);
        property(MinijaxProperties.SSL_KEY_STORE_PASSWORD, keyStorePassword);
        property(MinijaxProperties.SSL_KEY_MANAGER_PASSWORD, keyManagerPassword);
        return this;
    }

    public Minijax defaultCacheControl(final CacheControl defaultCacheControl) {
        getDefaultApplication().register(new MinijaxCacheControlFilter(defaultCacheControl));
        return this;
    }

    public Minijax host(final String host) {
        this.host = host;
        return this;
    }

    public String getHost() {
        return host;
    }

    public Minijax port(final int port) {
        this.port = port;
        return this;
    }

    public int getPort() {
        return port;
    }

    public void start(final int port) {
        this.port = port;
        start();
    }

    @SuppressWarnings("unchecked")
    public void start() {
        if (server == null) {
            try {
                final String serviceFile = "META-INF/services/org.minijax.MinijaxServer";
                final String className = IOUtils.toString(Minijax.class.getClassLoader().getResourceAsStream(serviceFile)).trim();
                final Class<? extends MinijaxServer> serverClass = (Class<? extends MinijaxServer>) Class.forName(className);
                final Constructor<? extends MinijaxServer> serverConstructor = serverClass.getConstructor(Minijax.class);
                server = serverConstructor.newInstance(this);
            } catch (final Exception ex) {
                throw new MinijaxException("Error creating server: " + ex.getMessage(), ex);
            }
        }
        server.start();
    }

    public void stop() {
        server.stop();
    }

    /*
     * Test helpers
     */

    public MinijaxTestWebTarget target(final String uri) {
        return new MinijaxTestClient(this).target(uri);
    }

    public MinijaxTestWebTarget target(final URI uri) {
        return new MinijaxTestClient(this).target(uri);
    }

    public MinijaxRequestContext createRequestContext() {
        return new MinijaxTestRequestContext(getDefaultApplication(), GET, "/");
    }

    public MinijaxRequestContext createRequestContext(final String method, final String uri) {
        return new MinijaxTestRequestContext(getDefaultApplication(), method, uri);
    }
}
