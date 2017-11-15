package org.minijax.security;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.Test;
import org.minijax.MinijaxRequestContext;
import org.minijax.db.DefaultBaseDao;
import org.minijax.test.MinijaxTest;

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


    @BeforeClass
    public static void setUpSecurityTest() throws IOException {
        resetServer();
        getServer()
                .registerPersistence()
                .register(new SecurityFeature(User.class, Dao.class))
                .register(RolesAllowedTest.class);

        try (final MinijaxRequestContext ctx = createRequestContext()) {
            final Dao dao = ctx.get(Dao.class);

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

            aliceCookie = ctx.get(Security.class).loginAs(alice);
            bobCookie = ctx.get(Security.class).loginAs(bob);
        }
    }


    @Test
    public void testAnonymous() throws IOException {
        assertEquals(200, target("/public").request().get().getStatus());
    }


    @Test
    public void testUnauthorized() throws Exception {
        assertEquals(401, target("/private").request().get().getStatus());
    }


    @Test
    public void testCookieLogin() throws Exception {
        assertEquals(200, target("/private").request().cookie(bobCookie).get().getStatus());
    }


    @Test
    public void testForbidden() throws Exception {
        assertEquals(403, target("/admin").request().cookie(bobCookie).get().getStatus());
    }


    @Test
    public void testAdminAccess() throws Exception {
        assertEquals(200, target("/admin").request().cookie(aliceCookie).get().getStatus());
    }


    @Test
    public void testLogin() throws Exception {
        try (final MinijaxRequestContext ctx = createRequestContext()) {
            final NewCookie cookie = ctx.get(Security.class).login("alice@example.com", "alicepwd");
            assertNotNull(cookie);
            assertNotNull(cookie.getValue());
        }
    }


    @Test(expected = BadRequestException.class)
    public void testLoginUserNotFound() throws Exception {
        try (final MinijaxRequestContext ctx = createRequestContext()) {
            ctx.get(Security.class).login("notfound@example.com", "alicepwd");
        }
    }


    @Test(expected = BadRequestException.class)
    public void testLoginIncorrectPassword() throws Exception {
        try (final MinijaxRequestContext ctx = createRequestContext()) {
            ctx.get(Security.class).login("alice@example.com", "wrong_password");
        }
    }
}
