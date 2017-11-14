package com.example.resources;

import static org.junit.Assert.*;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.minijax.view.View;

import com.example.PetClinicTest;

public class DefaultResourceTest extends PetClinicTest {

    @Before
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
