package org.minijax;

import static org.junit.Assert.*;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.junit.Test;

public class ResponseBuilderTest {

    @Test
    public void testDelegate() {
        final ResponseBuilder builder = Response.ok();
        assertTrue(builder instanceof MinijaxResponseBuilder);
    }
}
