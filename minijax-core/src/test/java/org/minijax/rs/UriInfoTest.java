package org.minijax.rs;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;

import org.junit.jupiter.api.Test;

public class UriInfoTest {

    @Test
    public void testBasic() {
        final MinijaxUriInfo uriInfo = new MinijaxUriInfo(URI.create("https://www.example.com/path/test?key=value"));
        assertEquals("https://www.example.com/path/test", uriInfo.getAbsolutePath().toString());
        assertEquals("https://www.example.com", uriInfo.getBaseUri().toString());
        assertEquals("/path/test", uriInfo.getPath());
        assertEquals("/path/test", uriInfo.getPath(true));
        assertEquals("https://www.example.com/path/test?key=value", uriInfo.getRequestUri().toString());

        assertEquals(2, uriInfo.getPathSegments().size());
        assertEquals("path", uriInfo.getPathSegments().get(0).getPath());
        assertEquals("test", uriInfo.getPathSegments().get(1).getPath());

        assertEquals(1, uriInfo.getQueryParameters().size());
        assertEquals("value", uriInfo.getQueryParameters().getFirst("key"));
    }
}
