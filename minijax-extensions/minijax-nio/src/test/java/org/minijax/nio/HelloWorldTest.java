package org.minijax.nio;

import static org.junit.Assert.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Test;
import org.minijax.Minijax;

public class HelloWorldTest {

    @Path("/")
    public static class HelloResource {

        @GET
        public static Response hello() {
            return Response.ok("Hello world!", MediaType.TEXT_PLAIN)
                    .header("X-foo", "bar")
                    .build();
        }

        @POST
        @Consumes("text/plain")
        public static Response echo(final String contentBody) {
            return Response.ok("You said: " + contentBody, MediaType.TEXT_PLAIN)
                    .build();
        }
    }

    @Test
    public void testHello() throws Exception {
        final Minijax minijax = new Minijax().register(HelloResource.class);
        final String request = "GET / HTTP/1.1\r\n";
        final MockSocketChannel channel = new MockSocketChannel(null, request);
        final Connection conn = new Connection(minijax, channel);
        conn.handle();

        final String expected = "HTTP/1.1 200\r\n" +
                "Content-Length: 12\r\n" +
                "X-foo: bar\r\n" +
                "\r\n" +
                "Hello world!";
        assertEquals(expected, channel.getOutputAsString());
    }

    @Test
    public void testHead() throws Exception {
        final Minijax minijax = new Minijax().register(HelloResource.class);
        final String request = "HEAD / HTTP/1.1\r\n";
        final MockSocketChannel channel = new MockSocketChannel(null, request);
        final Connection conn = new Connection(minijax, channel);
        conn.handle();

        final String expected = "HTTP/1.1 200\r\n" +
                "X-foo: bar\r\n" +
                "\r\n";
        assertEquals(expected, channel.getOutputAsString());
    }

    @Test
    public void testHttp10KeepAlive() throws Exception {
        final Minijax minijax = new Minijax().register(HelloResource.class);
        final String request = "HEAD / HTTP/1.0\r\n" +
                    "Connection: keep-alive\r\n";
        final MockSocketChannel channel = new MockSocketChannel(null, request);
        final Connection conn = new Connection(minijax, channel);
        conn.handle();

        final String expected = "HTTP/1.0 200\r\n" +
                "X-foo: bar\r\n" +
                "Connection: keep-alive\r\n" +
                "\r\n";
        assertEquals(expected, channel.getOutputAsString());
    }

    @Test
    public void testHttp11ConnectionClose() throws Exception {
        final Minijax minijax = new Minijax().register(HelloResource.class);
        final String request = "HEAD / HTTP/1.1\r\n" +
                    "Connection: close\r\n";
        final MockSocketChannel channel = new MockSocketChannel(null, request);
        final Connection conn = new Connection(minijax, channel);
        conn.handle();

        final String expected = "HTTP/1.1 200\r\n" +
                "X-foo: bar\r\n" +
                "Connection: close\r\n" +
                "\r\n";
        assertEquals(expected, channel.getOutputAsString());
    }

    @Test
    public void testEcho() throws Exception {
        final Minijax minijax = new Minijax().register(HelloResource.class);
        final String request = "POST / HTTP/1.1\r\n" +
                "Content-Length: 3\r\n" +
                "\r\n" +
                "xyz";
        final MockSocketChannel channel = new MockSocketChannel(null, request);
        final Connection conn = new Connection(minijax, channel);
        conn.handle();

        final String expected = "HTTP/1.1 200\r\n" +
                "Content-Length: 13\r\n" +
                "\r\n" +
                "You said: xyz";
        assertEquals(expected, channel.getOutputAsString());
    }
}
