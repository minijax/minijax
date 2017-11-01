package org.minijax;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;

import javax.servlet.http.Cookie;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.junit.Before;
import org.junit.Test;
import org.minijax.test.MinijaxTest;
import org.minijax.test.MockHttpServletRequest;
import org.minijax.test.MockHttpServletResponse;

public class ServletTest extends MinijaxTest {

    @GET
    @Path("/servlet")
    @Produces(MediaType.TEXT_PLAIN)
    public static String getText() {
        return "Hello world!";
    }

    @GET
    @Path("/null")
    @Produces(MediaType.TEXT_PLAIN)
    public static String getNull() {
        return null;
    }

    @Before
    public void setUp() {
        register(ServletTest.class);
    }

    @Test
    public void testServletResponse() throws IOException {
        final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        final Cookie[] cookies = new Cookie[0];

        final MockHttpServletRequest servletRequest = new MockHttpServletRequest("GET", URI.create("/servlet"), headers, null, cookies);
        final MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        final MinijaxRequestContext context = new MinijaxRequestContext(null, servletRequest, servletResponse);

        getServer().getDefaultApplication().handle(context, servletResponse);

        assertEquals(200, servletResponse.getStatus());
        assertEquals("text/plain", servletResponse.getContentType());
        assertEquals("Hello world!", servletResponse.getOutput().trim());
    }

    @Test
    public void testNullResponse() throws IOException {
        final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        final Cookie[] cookies = new Cookie[0];

        final MockHttpServletRequest servletRequest = new MockHttpServletRequest("GET", URI.create("/null"), headers, null, cookies);
        final MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        final MinijaxRequestContext context = new MinijaxRequestContext(null, servletRequest, servletResponse);

        getServer().getDefaultApplication().handle(context, servletResponse);

        assertEquals(404, servletResponse.getStatus());
        assertNull(servletResponse.getContentType());
    }
}
