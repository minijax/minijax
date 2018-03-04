package org.minijax;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.junit.Test;

import io.undertow.Undertow;

public class ServerTest {

    @Test
    public void testRun() throws Exception {
        final Minijax minijax = createMinijax();
        minijax.start();
    }


    @Test
    public void testRunDefaultPort() {
        final Minijax minijax = createMinijax();
        minijax.start();
    }


    @Test
    public void testStaticFile() {
        final Minijax minijax = createMinijax();
        minijax.staticFiles("static/hello.txt").start();
    }


    @Test
    public void testStaticDirectory() {
        final Minijax minijax = createMinijax();
        minijax.staticDirectories("static").start();
    }


    @Test
    public void testCreateHttpServer() throws Exception {
        final Undertow.Builder builder = new Minijax().createServer();
        assertNull(getBuilderSslContext(builder));
    }


    @Test
    public void testCreateHttpsServer() throws Exception {
        final Undertow.Builder builder = new Minijax().secure("keystore.jks", "certpassword", "certpassword").createServer();
        assertNotNull(getBuilderSslContext(builder));
    }


    @Test
    public void testGetSslContext() throws Exception {
        final Minijax minijax = new Minijax().secure("keystore.jks", "certpassword", "certpassword");
        final SSLContext sslContext = minijax.getSslContext();
        assertNotNull(sslContext);
    }


    @Test
    public void testNullSslContext() throws Exception {
        final Minijax minijax = new Minijax();
        final SSLContext sslContext = minijax.getSslContext();
        assertNull(sslContext);
    }


    @Test
    public void testEmptySslContext() throws Exception {
        final Minijax minijax = new Minijax().secure("", "", "");
        final SSLContext sslContext = minijax.getSslContext();
        assertNull(sslContext);
    }


    private Minijax createMinijax() {
        return new Minijax() {
            @Override
            protected Undertow.Builder createServer() {
                return Undertow.builder();
            }
        };
    }


    @SuppressWarnings("rawtypes")
    private SSLContext getBuilderSslContext(final Undertow.Builder builder) throws Exception {
        final Field listenersField = builder.getClass().getDeclaredField("listeners");
        listenersField.setAccessible(true);
        final List listeners = (List) listenersField.get(builder);
        final Object listener = listeners.get(0);
        final Field sslContextField = listener.getClass().getDeclaredField("sslContext");
        sslContextField.setAccessible(true);
        return (SSLContext) sslContextField.get(listener);
    }
}
