package org.minijax.security;

import static jakarta.ws.rs.core.MediaType.*;

import static org.junit.Assert.*;

import java.io.IOException;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.minijax.rs.MinijaxRequestContext;
import org.minijax.rs.persistence.PersistenceFeature;
import org.minijax.rs.test.MinijaxTest;

public class ChangePasswordTest extends MinijaxTest {

    @Inject
    private Security<User> security;

    @POST
    @Path("/changepassword")
    @Consumes(APPLICATION_FORM_URLENCODED)
    @RolesAllowed("user")
    public Response handleChangePassword(
            @FormParam("oldPassword") final String oldPassword,
            @FormParam("newPassword") final String newPassword,
            @FormParam("confirmNewPassword") final String confirmNewPassword) {
        final ChangePasswordResult result = security.changePassword(oldPassword, newPassword, confirmNewPassword);
        if (result != ChangePasswordResult.SUCCESS) {
            throw new BadRequestException();
        }
        return Response.ok().build();
    }

    @BeforeClass
    public static void setUpChangePasswordTest() throws IOException {
        register(PersistenceFeature.class);
        register(new SecurityFeature(User.class, Dao.class));
        register(ChangePasswordTest.class);
    }

    @Test
    public void testChangePasswordSuccess() throws IOException {
        final User user = new User();
        user.setName("Example 1");
        user.setEmail("pwd-1@example.com");
        user.setRoles("user");
        user.setPassword("my-old-password");

        Cookie cookie = null;

        try (MinijaxRequestContext ctx = createRequestContext()) {
            ctx.getResource(Dao.class).create(user);
            cookie = ctx.getResource(Security.class).loginAs(user);
        }

        final Form form = new Form();
        form.param("csrf", cookie.getValue());
        form.param("oldPassword", "my-old-password");
        form.param("newPassword", "my-new-password");
        form.param("confirmNewPassword", "my-new-password");

        final Response r = target("/changepassword").request().cookie(cookie).post(Entity.form(form));
        assertNotNull(r);
        assertEquals(200, r.getStatus());

        try (MinijaxRequestContext ctx = createRequestContext()) {
            final User check = ctx.getResource(Dao.class).read(User.class, user.getId());
            assertFalse(BCrypt.checkpw("my-old-password", check.getPasswordHash()));
            assertTrue(BCrypt.checkpw("my-new-password", check.getPasswordHash()));
        }
    }

    @Test
    public void testUserWithoutPassword() throws IOException {
        final User user = new User();
        user.setName("Example 2");
        user.setEmail("pwd-2@example.com");
        user.setRoles("user");

        Cookie cookie = null;

        try (MinijaxRequestContext ctx = createRequestContext()) {
            ctx.getResource(Dao.class).create(user);
            cookie = ctx.getResource(Security.class).loginAs(user);
        }

        final Form form = new Form();
        form.param("csrf", cookie.getValue());
        form.param("oldPassword", "my-old-password");
        form.param("newPassword", "my-new-password");
        form.param("confirmNewPassword", "my-new-password");

        final Response r = target("/changepassword").request().cookie(cookie).post(Entity.form(form));
        assertNotNull(r);
        assertEquals(400, r.getStatus());
    }

    @Test
    public void testIncorrectOldPassword() throws IOException {
        final User user = new User();
        user.setName("Example 3");
        user.setEmail("pwd-3@example.com");
        user.setRoles("user");
        user.setPassword("my-old-password");

        Cookie cookie = null;

        try (MinijaxRequestContext ctx = createRequestContext()) {
            ctx.getResource(Dao.class).create(user);
            cookie = ctx.getResource(Security.class).loginAs(user);
        }

        final Form form = new Form();
        form.param("csrf", cookie.getValue());
        form.param("oldPassword", "wrong-old-password");
        form.param("newPassword", "my-new-password");
        form.param("confirmNewPassword", "my-new-password");

        final Response r = target("/changepassword").request().cookie(cookie).post(Entity.form(form));
        assertNotNull(r);
        assertEquals(400, r.getStatus());
    }

    @Test
    public void testMismatchedPasswords() throws IOException {
        final User user = new User();
        user.setName("Example 4");
        user.setEmail("pwd-4@example.com");
        user.setRoles("user");
        user.setPassword("my-old-password");

        Cookie cookie = null;

        try (MinijaxRequestContext ctx = createRequestContext()) {
            ctx.getResource(Dao.class).create(user);
            cookie = ctx.getResource(Security.class).loginAs(user);
        }

        final Form form = new Form();
        form.param("csrf", cookie.getValue());
        form.param("oldPassword", "my-old-password");
        form.param("newPassword", "my-new-password");
        form.param("confirmNewPassword", "different-password");

        final Response r = target("/changepassword").request().cookie(cookie).post(Entity.form(form));
        assertNotNull(r);
        assertEquals(400, r.getStatus());
    }

    @Test
    public void testPasswordTooShort() throws IOException {
        final User user = new User();
        user.setName("Example 5");
        user.setEmail("pwd-5@example.com");
        user.setRoles("user");
        user.setPassword("my-old-password");

        Cookie cookie = null;

        try (MinijaxRequestContext ctx = createRequestContext()) {
            ctx.getResource(Dao.class).create(user);
            cookie = ctx.getResource(Security.class).loginAs(user);
        }

        final Form form = new Form();
        form.param("csrf", cookie.getValue());
        form.param("oldPassword", "my-old-password");
        form.param("newPassword", "foo");
        form.param("confirmNewPassword", "foo");

        final Response r = target("/changepassword").request().cookie(cookie).post(Entity.form(form));
        assertNotNull(r);
        assertEquals(400, r.getStatus());
    }
}
