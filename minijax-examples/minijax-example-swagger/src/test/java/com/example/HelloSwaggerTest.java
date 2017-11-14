package com.example;

import static javax.ws.rs.client.Entity.*;
import static javax.ws.rs.core.MediaType.*;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.minijax.json.JsonFeature;
import org.minijax.test.MinijaxTest;

import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.util.Json;

public class HelloSwaggerTest extends MinijaxTest {

    @Before
    public void setUp() {
        register(io.swagger.jaxrs.listing.ApiListingResource.class);
        register(JsonFeature.class);
        register(HelloSwagger.class);
    }


    @Test
    public void testShout() {
        assertEquals("HELLO", target("/api/shout").request().post(entity("hello", TEXT_PLAIN), String.class));
    }


    @Test
    public void testSwagger() throws IOException {
        final Response response = target("/swagger.json").request().get();
        assertNotNull(response);
        assertEquals(200, response.getStatus());

        final Swagger swagger = Json.mapper().readValue((String) response.getEntity(), Swagger.class);
        assertNotNull(swagger);

        final Path path = swagger.getPaths().get("/api/shout");
        assertNotNull(path);
    }
}
