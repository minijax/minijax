package minitwit;

import static org.junit.Assert.*;

import java.io.*;

import javax.ws.rs.core.*;

import org.junit.*;
import org.minijax.*;
import org.minijax.db.*;
import org.minijax.mustache.*;
import org.minijax.security.*;
import org.minijax.test.*;
import org.minijax.view.*;

import minitwit.Minitwit.Dao;
import minitwit.Minitwit.User;

public class MinitwitTest extends MinijaxTest {
    public static User alice;
    public static Cookie aliceCookie;
    public static User bob;
    public static Cookie bobCookie;

    @BeforeClass
    public static void setUpMinitwit() throws IOException {
        getServer()
                .property("javax.persistence.jdbc.url", "jdbc:h2:mem:test")
                .register(PersistenceFeature.class)
                .register(MustacheFeature.class)
                .register(new SecurityFeature(User.class, Dao.class))
                .register(Minitwit.class);

        try (final MinijaxRequestContext ctx = createRequestContext()) {
            final Dao dao = ctx.getApplication().getResource(Dao.class);
            alice = new User();
            alice.setName("Alice Smith");
            alice.setHandle("alice");
            alice.setEmail("alice@example.com");
            alice.setPassword("alicepwd");
            alice.setRoles("user");
            dao.create(alice);

            aliceCookie = ctx.get(Security.class).loginAs(alice);

            bob = new User();
            bob.setName("Bob Johnson");
            bob.setHandle("bob");
            bob.setEmail("bob@example.com");
            bob.setPassword("bobpwd");
            bob.setRoles("bob");
            dao.create(bob);

            bobCookie = ctx.get(Security.class).loginAs(bob);
        }
    }

    @Test
    public void testAnonymous() {
        final Response response = target("/").request().get();
        assertNotNull(response);
        assertEquals(303, response.getStatus());
        assertEquals("/public", response.getHeaderString("Location"));
    }

    @Test
    public void testPublicTimeline() {
        final Response response = target("/public").request().get();
        assertNotNull(response);
        assertEquals(200, response.getStatus());

        final View view = (View) response.getEntity();
        assertNotNull(view);
        assertEquals("timeline", view.getTemplateName());
        assertNotNull(view.getModel().get("messages"));
    }

    @Test
    public void testPersonalTimeline() throws Exception {
        final Response response = target("/").request().cookie(aliceCookie).get();
        assertNotNull(response);
        assertEquals(200, response.getStatus());

        final View view = (View) response.getEntity();
        assertNotNull(view);
        assertEquals("timeline", view.getTemplateName());
        assertNotNull(view.getModel().get("messages"));
    }

    @Test
    public void testDifferentUserTimeline() throws Exception {
        final Response response = target("/bob").request().cookie(aliceCookie).get();
        assertNotNull(response);
        assertEquals(200, response.getStatus());

        final View view = (View) response.getEntity();
        assertNotNull(view);
        assertEquals("timeline", view.getTemplateName());
        assertNotNull(view.getModel().get("messages"));
    }

    @Test
    public void testFollowUser() throws Exception {
        assertFalse(alice.following.contains(bob));

        final Response response = target("/bob/follow").request().cookie(aliceCookie).get();
        assertNotNull(response);
        assertEquals(303, response.getStatus());

        try (final MinijaxRequestContext ctx = createRequestContext()) {
            final Dao dao = ctx.getApplication().getResource(Dao.class);
            assertTrue(dao.read(User.class, alice.getId()).following.contains(bob));
        }
    }

    @Test
    public void testGravatarUrl() throws IOException {
        final User user = new User();
        user.setEmail("cody@ebberson.com");
        assertEquals("https://www.gravatar.com/avatar/9e46d967afb5e2fd13b2802c1e64112d?d=identicon&s=80", user.gravatarUrl());
    }
}
