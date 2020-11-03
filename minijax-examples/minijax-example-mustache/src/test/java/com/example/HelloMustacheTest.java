package com.example;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.minijax.mustache.MustacheFeature;
import org.minijax.rs.test.MinijaxTest;
import org.minijax.view.View;

public class HelloMustacheTest extends MinijaxTest {

    @BeforeEach
    public void setUp() {
        register(MustacheFeature.class);
        register(HelloMustache.class);
    }

    @Test
    public void testMustacheTemplate() {
        final Response response = target("/").request().get();
        assertNotNull(response);
        assertEquals(200, response.getStatus());

        final View view = (View) response.getEntity();
        assertNotNull(view);
        assertEquals("demo", view.getTemplateName());
        assertEquals("Chris", view.getModel().get("name"));

        final String str = response.readEntity(String.class);
        assertNotNull(str);
        assertEquals(
                "Hello Chris\n" +
                "You have just won 10000 dollars!\n" +
                "Well, 6000.0 dollars, after taxes.\n",
                str);
    }
}
