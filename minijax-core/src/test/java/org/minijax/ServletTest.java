package org.minijax;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.junit.Before;
import org.junit.Test;
import org.minijax.servlet.MockHttpServletRequest;
import org.minijax.servlet.MockHttpServletResponse;
import org.minijax.test.MinijaxTest;

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

        final MockHttpServletRequest servletRequest = new MockHttpServletRequest(headers, cookies, "GET", "/servlet", null);
        final MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        final MinijaxServletRequestContext context = new MinijaxServletRequestContext(servletRequest, servletResponse);

        getServer().handle(context, servletResponse);

        assertEquals(200, servletResponse.getStatus());
        assertEquals("text/plain", servletResponse.getContentType());
        assertEquals("Hello world!", servletResponse.getOutput().trim());
    }

    @Test
    public void testNullResponse() throws IOException {
        final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        final Cookie[] cookies = new Cookie[0];

        final MockHttpServletRequest servletRequest = new MockHttpServletRequest(headers, cookies, "GET", "/null", null);
        final MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        final MinijaxServletRequestContext context = new MinijaxServletRequestContext(servletRequest, servletResponse);

        getServer().handle(context, servletResponse);

        assertEquals(404, servletResponse.getStatus());
        assertNull(servletResponse.getContentType());
    }
}
