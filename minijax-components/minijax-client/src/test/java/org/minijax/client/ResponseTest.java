package org.minijax.client;

import static jakarta.ws.rs.core.MediaType.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response.Status;

import org.junit.Test;

public class ResponseTest {

    @Test
    public void testStatus() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
            assertEquals(200, response.getStatus());
            assertEquals(Status.OK, response.getStatusInfo());
        }
    }

    @Test
    public void testMediaType() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "Content-Type", "text/plain"))) {
            assertEquals(TEXT_PLAIN_TYPE, response.getMediaType());
        }
    }

    @Test
    public void testLocale() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "Content-Language", "en-US"))) {
            assertEquals(Locale.US, response.getLanguage());
        }
    }

    @Test
    public void testContentLength() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "Content-Length", "1024"))) {
            assertEquals(1024, response.getLength());
        }
    }

    /*
     * Unsupported
     */

    @Test(expected = UnsupportedOperationException.class)
    public void testReadEntity3() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
            response.readEntity(Object.class, new Annotation[0]);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testReadEntity4() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
            response.readEntity(new GenericType<Object>() {}, new Annotation[0]);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testHasEntity() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
            response.hasEntity();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBufferEntity() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
            response.bufferEntity();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetAllowedMethods() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
            response.getAllowedMethods();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetCookies() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
            response.getCookies();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetEntityTag() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
            response.getEntityTag();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDate() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
            response.getDate();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLastModified() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
            response.getLastModified();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLocation() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
            response.getLocation();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLinks() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
            response.getLinks();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testHasLink() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
            response.hasLink(null);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLink() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
            response.getLink(null);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLinkBuilder() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
            response.getLinkBuilder(null);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMetadata() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
            response.getMetadata();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetStringHeaders() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
            response.getStringHeaders();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetHeaderString() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
            response.getHeaderString(null);
        }
    }

    @SuppressWarnings("unchecked")
    private static HttpResponse<InputStream> mockResponse(final int statusCode, final String key, final String value) {
        final HttpHeaders httpHeaders = HttpHeaders.of(Map.of(key, Arrays.asList(value)), (x, y) -> true);
        final HttpResponse<InputStream> httpResponse = mock(HttpResponse.class);
        when(httpResponse.statusCode()).thenReturn(statusCode);
        when(httpResponse.headers()).thenReturn(httpHeaders);
        return httpResponse;
    }
}
