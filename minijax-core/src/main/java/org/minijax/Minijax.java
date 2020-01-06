package org.minijax;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.CacheControl;

import org.apache.commons.io.IOUtils;
import org.minijax.cdi.MinijaxInjector;
import org.minijax.rs.MinijaxApplicationContext;
import org.minijax.rs.MinijaxCacheControlFilter;
import org.minijax.rs.MinijaxException;
import org.minijax.rs.MinijaxProperties;
import org.minijax.rs.MinijaxServer;
import org.minijax.rs.MinijaxStaticResource;
import org.minijax.rs.util.ClassPathScanner;
import org.minijax.rs.util.OptionalClasses;
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
    private final List<MinijaxApplicationContext> applications;
    private MinijaxServer server;
    private String host;
    private int port;

    public Minijax() {
        applications = new ArrayList<>();
        host = DEFAULT_HOST;
        port = DEFAULT_PORT;
    }

    public List<MinijaxApplicationContext> getApplications() {
        return applications;
    }

    public MinijaxApplicationContext getDefaultApplication() {
        if (applications.isEmpty()) {
            applications.add(new MinijaxApplicationContext("/"));
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

    public MinijaxApplicationContext getApplication(final URI requestUri) {
        if (applications.size() == 1) {
            // Common case is only the default application
            return applications.get(0);
        }
        final String requestPath = requestUri.getPath();
        for (final MinijaxApplicationContext application : applications) {
            if (requestPath.startsWith(application.getPath())) {
                return application;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public Minijax register(final Class<?> componentClass) {
        if (javax.ws.rs.core.Application.class.isAssignableFrom(componentClass)) {
            applications.add(new MinijaxApplicationContext((Class<Application>) componentClass));
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
        getDefaultApplication().register(component);
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
        for (final String packageName : packageNames) {
            scanPackage(packageName);
        }
        return this;
    }

    private void scanPackage(final String packageName) {
        try {
            for (final Class<?> c : ClassPathScanner.scan(packageName)) {
                if (isAutoScanClass(c)) {
                    register(c);
                }
            }
        } catch (final IOException ex) {
            throw new MinijaxException(ex.getMessage(), ex);
        }
    }

    private boolean isAutoScanClass(final Class<?> c) {
        for (final Annotation a : c.getAnnotations()) {
            final Class<?> t = a.annotationType();
            if (t == javax.ws.rs.ext.Provider.class
                    || t == javax.ws.rs.ApplicationPath.class
                    || t == javax.ws.rs.Path.class
                    || t == OptionalClasses.SERVER_ENDPOINT) {
                return true;
            }
        }
        return false;
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
}
