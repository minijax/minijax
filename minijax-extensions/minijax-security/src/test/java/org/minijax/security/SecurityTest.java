package org.minijax.security;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.minijax.MinijaxRequestContext;
import org.minijax.data.DefaultBaseDao;
import org.minijax.test.MinijaxTest;

public class SecurityTest extends MinijaxTest {
    public static User alice;
    public static NewCookie aliceCookie;
    public static User bob;
    public static NewCookie bobCookie;


    @RequestScoped
    public static class Dao extends DefaultBaseDao implements SecurityDao {

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


    @Before
    public void setUp() {
        getServer()
                .registerPersistence()
                .register(new SecurityFeature(User.class))
                .register(Dao.class, SecurityDao.class)
                .register(SecurityTest.class);

        initDb();
    }


    public void initDb() {
        try (MinijaxRequestContext ctx = createRequestContext()) {
            final Dao dao = ctx.getApplication().get(Dao.class);
            if (dao.countAll(User.class) > 0) {
                return;
            }

            alice = new User();
            alice.setName("Alice");
            alice.setEmail("alice@example.com");
            alice.setHandle("alice");
            alice.setPasswordHash(BCrypt.hashpw("alicepwd", BCrypt.gensalt()));
            alice.setRoles("user", "admin");
            dao.create(alice);

            bob = new User();
            bob.setName("Bob");
            bob.setEmail("bob@example.com");
            bob.setHandle("bob");
            bob.setPasswordHash(BCrypt.hashpw("bobpwd", BCrypt.gensalt()));
            bob.setRoles("user");
            dao.create(bob);

            aliceCookie = ctx.get(Security.class).loginAs(alice);
            bobCookie = ctx.get(Security.class).loginAs(bob);

        } catch (final IOException ex) {
            ex.printStackTrace();
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
}
