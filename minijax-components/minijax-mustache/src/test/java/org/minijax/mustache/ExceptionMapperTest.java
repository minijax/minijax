package org.minijax.mustache;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.Test;
import org.minijax.rs.MinijaxRequestContext;
import org.minijax.rs.test.MinijaxTest;
import org.minijax.view.View;

class ExceptionMapperTest extends MinijaxTest {

    @Test
    void testNotFound() throws IOException {
        try (final MinijaxRequestContext ctx = createRequestContext()) {
            final MinijaxMustacheExceptionMapper mapper = new MinijaxMustacheExceptionMapper();
            final Response response = mapper.toResponse(new NotFoundException());
            assertNotNull(response);
            assertEquals(404, response.getStatus());
            assertEquals("error", ((View) response.getEntity()).getTemplateName());
        }
    }
}
