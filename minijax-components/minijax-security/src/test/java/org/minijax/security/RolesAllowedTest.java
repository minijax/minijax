package org.minijax.security;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.minijax.dao.DefaultBaseDao;
import org.minijax.rs.MinijaxRequestContext;
import org.minijax.rs.persistence.PersistenceFeature;
import org.minijax.rs.test.MinijaxTest;

public class RolesAllowedTest extends MinijaxTest {
    private static User alice;
    private static NewCookie aliceCookie;
    private static User bob;
    private static NewCookie bobCookie;

    @RequestScoped
    private static class Dao extends DefaultBaseDao implements SecurityDao {

    }

    @GET
    @Path("/public")
    public static Response getPublic() {
        return Response.ok().build();
    }

    @GET
    @Path("/private")
    @RolesAllowed("user")
    public static Response getPrivate() {
        return Response.ok().build();
    }

    @GET
    @Path("/admin")
    @RolesAllowed("admin")
    public static Response getAdmin() {
        return Response.ok().build();
    }

    @BeforeAll
    public static void setUpSecurityTest() throws IOException {
        resetServer();
        getServer()
                .register(PersistenceFeature.class)
                .register(new SecurityFeature(User.class, Dao.class))
                .register(RolesAllowedTest.class);

        try (final MinijaxRequestContext ctx = createRequestContext()) {
            final Dao dao = ctx.getResource(Dao.class);

            alice = new User();
            alice.setName("Alice");
            alice.setEmail("alice@example.com");
            alice.setHandle("alice");
            alice.setPassword("alicepwd");
            alice.setRoles("user", "admin");
            dao.create(alice);

            bob = new User();
            bob.setName("Bob");
            bob.setEmail("bob@example.com");
            bob.setHandle("bob");
            bob.setPassword("bobpwd");
            bob.setRoles("user");
            dao.create(bob);

            aliceCookie = ctx.getResource(Security.class).loginAs(alice);
            bobCookie = ctx.getResource(Security.class).loginAs(bob);
        }
    }

    @Test
    void testAnonymous() throws IOException {
        assertEquals(200, target("/public").request().get().getStatus());
    }

    @Test
    void testUnauthorized() throws Exception {
        assertEquals(401, target("/private").request().get().getStatus());
    }

    @Test
    void testCookieLogin() throws Exception {
        assertEquals(200, target("/private").request().cookie(bobCookie).get().getStatus());
    }

    @Test
    void testForbidden() throws Exception {
        assertEquals(403, target("/admin").request().cookie(bobCookie).get().getStatus());
    }

    @Test
    void testAdminAccess() throws Exception {
        assertEquals(200, target("/admin").request().cookie(aliceCookie).get().getStatus());
    }

    @Test
    void testLogin() throws Exception {
        try (final MinijaxRequestContext ctx = createRequestContext()) {
            final LoginResult result = ctx.getResource(Security.class).login("alice@example.com", "alicepwd");
            final NewCookie cookie = result.getCookie();
            assertNotNull(cookie);
            assertNotNull(cookie.getValue());
        }
    }

    @Test
    void testLoginUserNotFound() throws Exception {
        try (final MinijaxRequestContext ctx = createRequestContext()) {
            final LoginResult result = ctx.getResource(Security.class).login("notfound@example.com", "alicepwd");
            assertEquals(LoginResult.Status.NOT_FOUND, result.getStatus());
        }
    }

    @Test
    void testLoginIncorrectPassword() throws Exception {
        try (final MinijaxRequestContext ctx = createRequestContext()) {
            final LoginResult result = ctx.getResource(Security.class).login("alice@example.com", "wrong_password");
            assertEquals(LoginResult.Status.INCORRECT, result.getStatus());
        }
    }
}
