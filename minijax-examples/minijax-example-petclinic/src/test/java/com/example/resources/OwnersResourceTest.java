package com.example.resources;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.minijax.view.View;

import com.example.PetClinicTest;
import com.example.model.Owner;

class OwnersResourceTest extends PetClinicTest {

    @BeforeEach
    public void setUp() {
        register(OwnersResource.class);
    }

    @Test
    void testSearchPage() {
        final Response response = target("/owners/search").request().get();
        assertNotNull(response);
        assertEquals(200, response.getStatus());

        final View page = (View) response.getEntity();
        assertEquals("search", page.getTemplateName());
    }

    @Test
    void testEmptySearch() {
        final Response response = target("/owners?q=").request().get();
        assertNotNull(response);
        assertEquals(200, response.getStatus());

        final View page = (View) response.getEntity();
        assertEquals("owners", page.getTemplateName());

        @SuppressWarnings("unchecked")
        final List<Owner> vets = (List<Owner>) page.getModel().get("owners");
        assertTrue(vets.size() >= 10);
    }

    @Test
    void testGeorgeSearch() {
        final Response response = target("/owners?q=george").request().get();
        assertNotNull(response);
        assertEquals(200, response.getStatus());

        final View page = (View) response.getEntity();
        assertEquals("owners", page.getTemplateName());

        @SuppressWarnings("unchecked")
        final List<Owner> vets = (List<Owner>) page.getModel().get("owners");
        assertEquals(1, vets.size());
        assertEquals("George Franklin", vets.get(0).getName());
    }

    @Test
    void testNewOwner() {
        final Response r1 = target("/owners/new").request().get();
        assertEquals(200, r1.getStatus());
        assertEquals("newowner", ((View) r1.getEntity()).getTemplateName());

        final Form form = new Form()
                .param("name", "Barack Obama")
                .param("address", "1600 Penn Ave")
                .param("city", "Washington DC")
                .param("telephone", "800-555-5555");

        final Response r2 = target("/owners/new").request().post(Entity.form(form));
        assertNotNull(r2);
        assertEquals(303, r2.getStatus());
        assertNotNull(r2.getHeaderString("Location"));

        final Response r3 = target(r2.getHeaderString("Location")).request().get();
        assertNotNull(r3);
        assertEquals(200, r3.getStatus());

        final View view = (View) r3.getEntity();
        final Owner owner = (Owner) view.getModel().get("owner");
        assertEquals("Barack Obama", owner.getName());
    }

    @Test
    void testOwnerNotFound() {
        final Response response = target("/owners/00000000-0000-0000-0000-000000000000").request().get();
        assertNotNull(response);
        assertEquals(404, response.getStatus());
    }

    @Test
    void testEditOwner() {
        final UUID georgeId = UUID.fromString("015f1b3d-b336-7e66-671a-7bfb8602b47f");

        final Response r1 = target("/owners/" + georgeId + "/edit").request().get();
        assertEquals(200, r1.getStatus());
        assertEquals("editowner", ((View) r1.getEntity()).getTemplateName());

        final Form form = new Form()
                .param("name", "George Franklin")
                .param("address", "New Address")
                .param("city", "Washington DC")
                .param("telephone", "800-555-5555");

        final Response r2 = target("/owners/" + georgeId + "/edit").request().post(Entity.form(form));
        assertNotNull(r2);
        assertEquals(303, r2.getStatus());
        assertNotNull(r2.getHeaderString("Location"));

        final Response r3 = target(r2.getHeaderString("Location")).request().get();
        assertNotNull(r3);
        assertEquals(200, r3.getStatus());

        final View view = (View) r3.getEntity();
        final Owner owner = (Owner) view.getModel().get("owner");
        assertEquals("New Address", owner.getAddress());
    }

    @Test
    void testEditOwnerNotFound() {
        final Response r1 = target("/owners/00000000-0000-0000-0000-000000000000/edit").request().get();
        assertNotNull(r1);
        assertEquals(404, r1.getStatus());

        final Response r2 = target("/owners/00000000-0000-0000-0000-000000000000/edit").request().post(Entity.form(new Form()));
        assertNotNull(r2);
        assertEquals(404, r2.getStatus());
    }
}
