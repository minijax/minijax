package org.minijax.rs;

import static jakarta.ws.rs.core.MediaType.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.minijax.rs.test.MinijaxTest;

class FileWriterTest extends MinijaxTest {

    @GET
    @Path("/file")
    public static File getFile() {
        return new File("src/test/resources/static/hello.txt");
    }

    @BeforeAll
    public static void setUpFileWriterTest() {
        resetServer();
        register(FileWriterTest.class);
    }

    @Test
    void testStaticDirectoryFile() {
        final Response response = target("/file").request().get();
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertEquals(TEXT_PLAIN_TYPE, response.getMediaType());

        final String str = response.readEntity(String.class);
        assertNotNull(str);
        assertEquals("Hello world!\n", str);
    }
}
