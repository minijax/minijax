package com.example.resources;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.minijax.view.View;

import com.example.PetClinicTest;
import com.example.model.Vet;

class VetsResourceTest extends PetClinicTest {

    @BeforeEach
    public void setUp() {
        register(VetsResource.class);
    }

    @Test
    void testVetsPage() {
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
