package org.minijax.client;

import static jakarta.ws.rs.core.MediaType.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response.Status;

import org.junit.jupiter.api.Test;

class ResponseTest {

    @Test
    void testStatus() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
            assertEquals(200, response.getStatus());
            assertEquals(Status.OK, response.getStatusInfo());
        }
    }

    @Test
    void testMediaType() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(
                mockResponse(200, "Content-Type", "text/plain"))) {
            assertEquals(TEXT_PLAIN_TYPE, response.getMediaType());
        }
    }

    @Test
    void testLocale() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(
                mockResponse(200, "Content-Language", "en-US"))) {
            assertEquals(Locale.US, response.getLanguage());
        }
    }

    @Test
    void testContentLength() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(
                mockResponse(200, "Content-Length", "1024"))) {
            assertEquals(1024, response.getLength());
        }
    }

    /*
     * Unsupported
     */

    @Test
    void testReadEntity3() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
            assertThrows(UnsupportedOperationException.class,
                    () -> response.readEntity(Object.class, new Annotation[0]));
        }
    }

    @Test
    void testReadEntity4() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
            assertThrows(UnsupportedOperationException.class, () -> response.readEntity(new GenericType<Object>() {
            }, new Annotation[0]));
        }
    }

    @Test
    void testHasEntity() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
            assertThrows(UnsupportedOperationException.class, () -> response.hasEntity());
        }
    }

    @Test
    void testBufferEntity() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
            assertThrows(UnsupportedOperationException.class, () -> response.bufferEntity());
        }
    }

    @Test
    void testGetAllowedMethods() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
            assertThrows(UnsupportedOperationException.class, () -> response.getAllowedMethods());
        }
    }

    @Test
    void testGetCookies() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
            assertThrows(UnsupportedOperationException.class, () -> response.getCookies());
        }
    }

    @Test
    void testGetEntityTag() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
            assertThrows(UnsupportedOperationException.class, () -> response.getEntityTag());
        }
    }

    @Test
    void testGetDate() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
            assertThrows(UnsupportedOperationException.class, () -> response.getDate());
        }
    }

    @Test
    void testGetLastModified() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
            assertThrows(UnsupportedOperationException.class, () -> response.getLastModified());
        }
    }

    @Test
    void testGetLocation() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
            assertThrows(UnsupportedOperationException.class, () -> response.getLocation());
        }
    }

    @Test
    void testGetLinks() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
            assertThrows(UnsupportedOperationException.class, () -> response.getLinks());
        }
    }

    @Test
    void testHasLink() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
            assertThrows(UnsupportedOperationException.class, () -> response.hasLink(null));
        }
    }

    @Test
    void testGetLink() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
            assertThrows(UnsupportedOperationException.class, () -> response.getLink(null));
        }
    }

    @Test
    void testGetLinkBuilder() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
            assertThrows(UnsupportedOperationException.class, () -> response.getLinkBuilder(null));
        }
    }

    @Test
    void testGetMetadata() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
            assertThrows(UnsupportedOperationException.class, () -> response.getMetadata());
        }
    }

    @Test
    void testGetStringHeaders() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
            assertThrows(UnsupportedOperationException.class, () -> response.getStringHeaders());
        }
    }

    @Test
    void testGetHeaderString() {
        try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
            assertThrows(UnsupportedOperationException.class, () -> response.getHeaderString(null));
        }
    }

    @SuppressWarnings("unchecked")
    private static HttpResponse<InputStream> mockResponse(final int statusCode, final String key, final String value) {
        final HttpHeaders httpHeaders = HttpHeaders.of(Map.of(key, Collections.singletonList(value)), (x, y) -> true);
        final HttpResponse<InputStream> httpResponse = mock(HttpResponse.class);
        when(httpResponse.statusCode()).thenReturn(statusCode);
        when(httpResponse.headers()).thenReturn(httpHeaders);
        return httpResponse;
    }
}
