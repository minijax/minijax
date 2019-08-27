package org.minijax;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.core.CacheControl;

import org.minijax.cdi.MinijaxInjector;
import org.minijax.util.UrlUtils;

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
    private final MinijaxApplicationContext defaultApplication;
    private final List<MinijaxApplicationContext> applications;
    private MinijaxServer server;

    public Minijax() {
        defaultApplication = new MinijaxApplicationContext("/");
        applications = new ArrayList<>();
        applications.add(defaultApplication);
    }


    public MinijaxInjector getInjector() {
        return defaultApplication.getInjector();
    }


    public <T> T getResource(final Class<T> c) {
        return defaultApplication.getResource(c);
    }


    public Map<String, Object> getProperties() {
        return defaultApplication.getProperties();
    }


    public Minijax property(final String name, final Object value) {
        defaultApplication.property(name, value);
        return this;
    }


    public Minijax properties(final Map<String, String> props) {
        defaultApplication.properties(props);
        return this;
    }


    public Minijax properties(final Properties props) {
        defaultApplication.properties(props);
        return this;
    }


    public Minijax properties(final File file) throws IOException {
        defaultApplication.properties(file);
        return this;
    }


    public Minijax properties(final InputStream inputStream) throws IOException {
        defaultApplication.properties(inputStream);
        return this;
    }


    public Minijax properties(final String fileName) throws IOException {
        defaultApplication.properties(fileName);
        return this;
    }


    public MinijaxApplicationContext getDefaultApplication() {
        return defaultApplication;
    }


    public MinijaxApplicationContext getApplication(final URI requestUri) {
        final String requestPath = requestUri.getPath();
        for (final MinijaxApplicationContext application : applications) {
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


    public String getHost() {
        return (String) defaultApplication.getProperties().getOrDefault(MinijaxProperties.HOST, DEFAULT_HOST);
    }


    public int getPort() {
        return Integer.parseInt((String) defaultApplication.getProperties().getOrDefault(MinijaxProperties.PORT, DEFAULT_PORT));
    }


    public void start(final int port) {
        property(MinijaxProperties.PORT, Integer.toString(port));
        start();
    }

    @SuppressWarnings("unchecked")
    public void start() {
        if (server == null) {
            try {
                final Class<? extends MinijaxServer> serverClass = (Class<? extends MinijaxServer>) Class.forName("org.minijax.undertow.MinijaxUndertowServer");
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
}
