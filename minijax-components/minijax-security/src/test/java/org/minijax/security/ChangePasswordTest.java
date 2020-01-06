package org.minijax.security;

import static javax.ws.rs.core.MediaType.*;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.minijax.dao.PersistenceFeature;
import org.minijax.rs.MinijaxRequestContext;
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
