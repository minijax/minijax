package org.minijax.mustache;

import static org.junit.Assert.*;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

import org.junit.Test;

public class ExceptionMapperTest {

    @Test
    public void testNotFound() {
        final MinijaxMustacheExceptionMapper mapper = new MinijaxMustacheExceptionMapper();
        final Response response = mapper.toResponse(new NotFoundException());
        assertNotNull(response);
        assertEquals(404, response.getStatus());
        assertEquals("error", ((View) response.getEntity()).getTemplateName());
    }
}
