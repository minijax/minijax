package org.minijax.delegates;

import static org.junit.Assert.*;

import javax.ws.rs.core.UriBuilder;

import org.junit.Test;
import org.minijax.uri.JerseyUriBuilder;

public class UriBuilderTest {

    @Test
    public void testDelegate() {
        assertTrue(UriBuilder.fromUri("http://www.example.com/") instanceof JerseyUriBuilder);
    }

    @Test
    public void testHost() {
        assertEquals("//localhost/", UriBuilder.fromPath("/").host("localhost").build().toString());
    }

    @Test
    public void testHostAndPort() {
        assertEquals("//localhost:8080/", UriBuilder.fromPath("/").host("localhost").port(8080).build().toString());
    }

    @Test
    public void testIpv4() {
        assertEquals("//127.0.0.1/", UriBuilder.fromPath("/").host("127.0.0.1").build().toString());
    }

    @Test
    public void testIpv6() {
        assertEquals("//[::1]/", UriBuilder.fromPath("/").host("[::1]").build().toString());
    }

    @Test
    public void testClone() {
        final UriBuilder b = UriBuilder.fromPath("/");
        assertTrue(b != b.clone());
    }
}
