package com.example.resources;

import static org.junit.Assert.*;

import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.minijax.view.View;

import com.example.PetClinicTest;
import com.example.model.Vet;

public class VetsResourceTest extends PetClinicTest {

    @Before
    public void setUp() {
        register(VetsResource.class);
    }

    @Test
    public void testVetsPage() {
        final Response response = target("/vets").request().get();
        assertNotNull(response);
        assertEquals(200, response.getStatus());

        final View page = (View) response.getEntity();
        assertEquals("vets", page.getTemplateName());

        @SuppressWarnings("unchecked")
        final List<Vet> vets = (List<Vet>) page.getModel().get("vets");
        assertEquals(6, vets.size());
    }
}
