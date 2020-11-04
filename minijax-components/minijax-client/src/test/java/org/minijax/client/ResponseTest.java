package org.minijax.client;

import static jakarta.ws.rs.core.MediaType.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.util.Arrays;
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
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
                response.readEntity(Object.class, new Annotation[0]);
            }
        });
    }

    @Test
    void testReadEntity4() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
                response.readEntity(new GenericType<Object>() {
                }, new Annotation[0]);
            }
        });
    }

    @Test
    void testHasEntity() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
                response.hasEntity();
            }
        });
    }

    @Test
    void testBufferEntity() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
                response.bufferEntity();
            }
        });
    }

    @Test
    void testGetAllowedMethods() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
                response.getAllowedMethods();
            }
        });
    }

    @Test
    void testGetCookies() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
                response.getCookies();
            }
        });
    }

    @Test
    void testGetEntityTag() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
                response.getEntityTag();
            }
        });
    }

    @Test
    void testGetDate() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
                response.getDate();
            }
        });
    }

    @Test
    void testGetLastModified() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
                response.getLastModified();
            }
        });
    }

    @Test
    void testGetLocation() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
                response.getLocation();
            }
        });
    }

    @Test
    void testGetLinks() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
                response.getLinks();
            }
        });
    }

    @Test
    void testHasLink() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
                response.hasLink(null);
            }
        });
    }

    @Test
    void testGetLink() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
                response.getLink(null);
            }
        });
    }

    @Test
    void testGetLinkBuilder() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
                response.getLinkBuilder(null);
            }
        });
    }

    @Test
    void testGetMetadata() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
                response.getMetadata();
            }
        });
    }

    @Test
    void testGetStringHeaders() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
                response.getStringHeaders();
            }
        });
    }

    @Test
    void testGetHeaderString() {
        assertThrows(UnsupportedOperationException.class, () -> {
            try (final MinijaxClientResponse response = new MinijaxClientResponse(mockResponse(200, "key", "value"))) {
                response.getHeaderString(null);
            }
        });
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
