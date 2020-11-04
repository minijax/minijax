package org.minijax.security;

import static jakarta.ws.rs.core.MediaType.*;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.minijax.rs.MinijaxRequestContext;
import org.minijax.rs.persistence.PersistenceFeature;
import org.minijax.rs.test.MinijaxTest;

public class CsrfFilterTest extends MinijaxTest {
    private static User user;
    private static NewCookie cookie;

    @POST
    @Path("/public_form")
    @Consumes(APPLICATION_FORM_URLENCODED)
    public static Response handlePublicForm(final MultivaluedMap<String, String> form) {
        return Response.ok().build();
    }

    @POST
    @Path("/private_form")
    @Consumes(APPLICATION_FORM_URLENCODED)
    @RolesAllowed("user")
    public static Response handlePrivateForm(final MultivaluedMap<String, String> form) {
        return Response.ok().build();
    }

    @BeforeAll
    @SuppressWarnings("unchecked")
    public static void setUpCsrfFilterTest() throws IOException {
        register(PersistenceFeature.class);
        register(new SecurityFeature(User.class, Dao.class));
        register(CsrfFilterTest.class);

        try (MinijaxRequestContext ctx = createRequestContext()) {
            user = new User();
            user.setName("Alice");
            user.setEmail("alice_csrf@example.com");
            user.setRoles("user");

            final Dao dao = ctx.getResource(Dao.class);
            user = dao.create(user);

            final Security<User> security = ctx.getResource(Security.class);
            cookie = security.loginAs(user);
        }
    }

    @Test
    void testPublic() {
        final Form form = new Form();

        final Response r = target("/public_form").request().post(Entity.form(form));
        assertNotNull(r);
        assertEquals(200, r.getStatus());
    }

    @Test
    void testPrivateFormWithoutUser() {
        final Form form = new Form();

        final Response r = target("/private_form").request().post(Entity.form(form));
        assertNotNull(r);
        assertEquals(401, r.getStatus());
    }

    @Test
    void testPrivateFormWithoutCsrf() {
        final Form form = new Form();

        final Response r = target("/private_form").request().cookie(cookie).post(Entity.form(form));
        assertNotNull(r);
        assertEquals(400, r.getStatus());
    }

    @Test
    void testPrivateFormWithCsrf() {
        final Form form = new Form();
        form.param("csrf", cookie.getValue());

        final Response r = target("/private_form").request().cookie(cookie).post(Entity.form(form));
        assertNotNull(r);
        assertEquals(200, r.getStatus());
    }
}
