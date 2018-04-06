package org.minijax.client;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.lang.annotation.Annotation;
import java.util.Locale;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicHeader;
import org.junit.Test;

public class ResponseTest {

    @Test
    public void testMediaType() {
        final CloseableHttpResponse innerResponse = mock(CloseableHttpResponse.class);
        when(innerResponse.getLastHeader(eq("Content-Type"))).thenReturn(new BasicHeader("Content-Type", "text/plain"));

        try (final MinijaxClientResponse response = new MinijaxClientResponse(innerResponse)) {
            assertEquals(MediaType.TEXT_PLAIN_TYPE, response.getMediaType());
        }
    }

    @Test
    public void testLocale() {
        final CloseableHttpResponse innerResponse = mock(CloseableHttpResponse.class);
        when(innerResponse.getLocale()).thenReturn(Locale.US);

        try (final MinijaxClientResponse response = new MinijaxClientResponse(innerResponse)) {
            assertEquals(Locale.US, response.getLanguage());
        }
    }

    @Test
    public void testContentLength() {
        final CloseableHttpResponse innerResponse = mock(CloseableHttpResponse.class);
        when(innerResponse.getLastHeader(eq("Content-Length"))).thenReturn(new BasicHeader("Content-Length", "1024"));

        try (final MinijaxClientResponse response = new MinijaxClientResponse(innerResponse)) {
            assertEquals(1024, response.getLength());
        }
    }

    /*
     * Unsupported
     */

    @Test(expected = UnsupportedOperationException.class)
    public void testReadEntity3() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(null)) {
            response.readEntity(Object.class, new Annotation[0]);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testReadEntity4() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(null)) {
            response.readEntity(new GenericType<Object>() {}, new Annotation[0]);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testHasEntity() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(null)) {
            response.hasEntity();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBufferEntity() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(null)) {
            response.bufferEntity();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetAllowedMethods() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(null)) {
            response.getAllowedMethods();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetCookies() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(null)) {
            response.getCookies();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetEntityTag() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(null)) {
            response.getEntityTag();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDate() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(null)) {
            response.getDate();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLastModified() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(null)) {
            response.getLastModified();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLocation() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(null)) {
            response.getLocation();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLinks() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(null)) {
            response.getLinks();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testHasLink() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(null)) {
            response.hasLink(null);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLink() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(null)) {
            response.getLink(null);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLinkBuilder() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(null)) {
            response.getLinkBuilder(null);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMetadata() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(null)) {
            response.getMetadata();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetStringHeaders() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(null)) {
            response.getStringHeaders();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetHeaderString() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(null)) {
            response.getHeaderString(null);
        }
    }
}
