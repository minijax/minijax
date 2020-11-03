package com.example.resources;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.minijax.view.View;

import com.example.PetClinicTest;

public class DefaultResourceTest extends PetClinicTest {

    @BeforeEach
    public void setUp() {
        register(DefaultResource.class);
    }

    @Test
    public void testHomePage() {
        final Response response = target("/").request().get();
        assertNotNull(response);
        assertEquals(200, response.getStatus());

        final View page = (View) response.getEntity();
        assertEquals("home", page.getTemplateName());
    }
}
