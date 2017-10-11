package org.minijax.json;

import static org.junit.Assert.*;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

import org.junit.Test;

public class ExceptionMapperTest {

    @Test
    public void testNotFound() {
        final MinijaxJsonExceptionMapper mapper = new MinijaxJsonExceptionMapper();
        final Response response = mapper.toResponse(new NotFoundException());
        assertNotNull(response);
        assertEquals(404, response.getStatus());
        assertEquals(404, ((MinijaxJsonExceptionWrapper) response.getEntity()).getCode());
        assertEquals("HTTP 404 Not Found", ((MinijaxJsonExceptionWrapper) response.getEntity()).getMessage());
    }
}
