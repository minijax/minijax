package org.minijax.mustache;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

import org.junit.Test;
import org.minijax.MinijaxRequestContext;
import org.minijax.test.MinijaxTest;
import org.minijax.view.View;

public class ExceptionMapperTest extends MinijaxTest {

    @Test
    public void testNotFound() throws IOException {
        try (final MinijaxRequestContext ctx = createRequestContext()) {
            final MinijaxMustacheExceptionMapper mapper = new MinijaxMustacheExceptionMapper();
            final Response response = mapper.toResponse(new NotFoundException());
            assertNotNull(response);
            assertEquals(404, response.getStatus());
            assertEquals("error", ((View) response.getEntity()).getTemplateName());
        }
    }
}
