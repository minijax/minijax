package org.minijax.nio;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.Socket;

import javax.ws.rs.GET;
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
    }

    @Test
    public void testHello() throws Exception {
        final Minijax minijax = new Minijax().register(HelloResource.class);

        final MinijaxNioServer server = new MinijaxNioServer(minijax);

        final String request = "GET / HTTP/1.1\r\n";

        final ByteArrayInputStream inputStream = new ByteArrayInputStream(request.getBytes());
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        final Socket socket = mock(Socket.class);
        when(socket.getInputStream()).thenReturn(inputStream);
        when(socket.getOutputStream()).thenReturn(outputStream);

        server.handleRequest(socket);

        final String expected = "HTTP/1.1 200\r\n" +
                "Content-Length: 12\r\n" +
                "X-foo: bar\r\n" +
                "\r\n" +
                "Hello world!";
        assertEquals(expected, outputStream.toString());
    }
}
