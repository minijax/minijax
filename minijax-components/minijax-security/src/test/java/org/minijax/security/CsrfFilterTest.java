package org.minijax.security;

import static javax.ws.rs.core.MediaType.*;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.Test;
import org.minijax.dao.PersistenceFeature;
import org.minijax.rs.MinijaxRequestContext;
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

    @BeforeClass
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
    public void testPublic() {
        final Form form = new Form();

        final Response r = target("/public_form").request().post(Entity.form(form));
        assertNotNull(r);
        assertEquals(200, r.getStatus());
    }

    @Test
    public void testPrivateFormWithoutUser() {
        final Form form = new Form();

        final Response r = target("/private_form").request().post(Entity.form(form));
        assertNotNull(r);
        assertEquals(401, r.getStatus());
    }

    @Test
    public void testPrivateFormWithoutCsrf() {
        final Form form = new Form();

        final Response r = target("/private_form").request().cookie(cookie).post(Entity.form(form));
        assertNotNull(r);
        assertEquals(400, r.getStatus());
    }

    @Test
    public void testPrivateFormWithCsrf() {
        final Form form = new Form();
        form.param("csrf", cookie.getValue());

        final Response r = target("/private_form").request().cookie(cookie).post(Entity.form(form));
        assertNotNull(r);
        assertEquals(200, r.getStatus());
    }
}
