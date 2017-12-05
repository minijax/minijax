package org.minijax.security;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.minijax.MinijaxRequestContext;
import org.minijax.db.PersistenceFeature;
import org.minijax.test.MinijaxTest;

public class ResetPasswordTest extends MinijaxTest {

    @Inject
    private Security<User> security;


    @POST
    @Path("/resetpassword/{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response handleSubmit(
            @PathParam("id") final String resetId,
            @FormParam("newPassword") final String newPassword,
            @FormParam("confirmNewPassword") final String confirmNewPassword) {

        final NewCookie cookie = security.resetPassword(resetId, newPassword, confirmNewPassword);
        return Response.ok().cookie(cookie).build();
    }


    @BeforeClass
    public static void setUpResetPasswordTest() throws IOException {
        register(PersistenceFeature.class);
        register(new SecurityFeature(User.class, Dao.class));
        register(ResetPasswordTest.class);
    }


    @Test
    public void testResetPasswordNotFound() throws IOException {
        final String code = "does-not-exist";

        final Form form = new Form();
        form.param("newPassword", "my-new-password");
        form.param("confirmNewPassword", "my-new-password");

        final Response r = target("/resetpassword/" + code).request().post(Entity.form(form));
        assertNotNull(r);
        assertEquals(404, r.getStatus());
        assertTrue(r.getCookies().isEmpty());
    }


    @Test
    public void testResetPasswordSuccess() throws IOException {
        final User user = new User();
        user.setName("Example 1");
        user.setEmail("reset-1@example.com");
        user.setRoles("user");

        String code = null;

        try (MinijaxRequestContext ctx = createRequestContext()) {
            ctx.get(Dao.class).create(user);
            code = ctx.get(Security.class).forgotPassword(user);
        }

        final Form form = new Form();
        form.param("newPassword", "my-new-password");
        form.param("confirmNewPassword", "my-new-password");

        final Response r = target("/resetpassword/" + code).request().post(Entity.form(form));
        assertNotNull(r);
        assertEquals(200, r.getStatus());
        assertFalse(r.getCookies().isEmpty());

        try (MinijaxRequestContext ctx = createRequestContext()) {
            final User check = ctx.get(Dao.class).read(User.class, user.getId());
            assertFalse(BCrypt.checkpw("my-old-password", check.getPasswordHash()));
            assertTrue(BCrypt.checkpw("my-new-password", check.getPasswordHash()));
        }
    }


    @Test
    public void testResetPasswordMismatch() throws IOException {
        final User user = new User();
        user.setName("Example 2");
        user.setEmail("reset-2@example.com");
        user.setRoles("user");

        String code = null;

        try (MinijaxRequestContext ctx = createRequestContext()) {
            ctx.get(Dao.class).create(user);
            code = ctx.get(Security.class).forgotPassword(user);
        }

        final Form form = new Form();
        form.param("newPassword", "my-new-password");
        form.param("confirmNewPassword", "different-password");

        final Response r = target("/resetpassword/" + code).request().post(Entity.form(form));
        assertNotNull(r);
        assertEquals(400, r.getStatus());
        assertTrue(r.getCookies().isEmpty());
    }


    @Test
    public void testResetPasswordTooShort() throws IOException {
        final User user = new User();
        user.setName("Example 3");
        user.setEmail("reset-3@example.com");
        user.setRoles("user");

        String code = null;

        try (MinijaxRequestContext ctx = createRequestContext()) {
            ctx.get(Dao.class).create(user);
            code = ctx.get(Security.class).forgotPassword(user);
        }

        final Form form = new Form();
        form.param("newPassword", "foo");
        form.param("confirmNewPassword", "foo");

        final Response r = target("/resetpassword/" + code).request().post(Entity.form(form));
        assertNotNull(r);
        assertEquals(400, r.getStatus());
        assertTrue(r.getCookies().isEmpty());
    }
}