package org.minijax.json;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.Test;
import org.minijax.rs.MinijaxRequestContext;
import org.minijax.rs.test.MinijaxTest;

class ExceptionMapperTest extends MinijaxTest {

    @Test
    void testNotFound() throws IOException {
        try (final MinijaxRequestContext ctx = createRequestContext()) {
            final MinijaxJsonExceptionMapper mapper = new MinijaxJsonExceptionMapper();
            final Response response = mapper.toResponse(new NotFoundException());
            assertNotNull(response);
            assertEquals(404, response.getStatus());
            assertEquals(404, ((MinijaxJsonExceptionWrapper) response.getEntity()).getCode());
            assertEquals("HTTP 404 Not Found", ((MinijaxJsonExceptionWrapper) response.getEntity()).getMessage());
        }
    }
}
