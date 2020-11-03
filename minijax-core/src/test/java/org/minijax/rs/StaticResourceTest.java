package org.minijax.rs;

import static jakarta.ws.rs.core.MediaType.*;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.minijax.rs.test.MinijaxTest;

public class StaticResourceTest extends MinijaxTest {

    @BeforeAll
    public static void setUpStaticResourceTest() {
        resetServer();
        getServer()
                .staticFiles("config.properties")
                .staticDirectories("static");
    }

    @Test
    public void testStaticFile() {
        final Response response = target("/config.properties").request().get();
        assertNotNull(response);
        assertEquals(200, response.getStatus());

        final String str = response.readEntity(String.class);
        assertNotNull(str);
        assertEquals("a=b\n", str);
    }

    @Test
    public void testStaticDirectory() {
        final Response response = target("/static").request().get();
        assertNotNull(response);
        assertEquals(404, response.getStatus());
    }

    @Test
    public void testStaticDirectoryTrailingSlash() {
        final Response response = target("/static/").request().get();
        assertNotNull(response);
        assertEquals(404, response.getStatus());
    }

    @Test
    public void testStaticDirectoryFile() {
        final Response response = target("/static/hello.txt").request().get();
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertEquals(TEXT_PLAIN_TYPE, response.getMediaType());

        final String str = response.readEntity(String.class);
        assertNotNull(str);
        assertEquals("Hello world!\n", str);
    }

    @Test
    public void testStaticDirectoryFileNotFound() {
        final Response response = target("/static/notfound.txt").request().get();
        assertNotNull(response);
        assertEquals(404, response.getStatus());
    }
}
