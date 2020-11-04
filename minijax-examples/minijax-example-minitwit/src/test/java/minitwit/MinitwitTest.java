package minitwit;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.minijax.mustache.*;
import org.minijax.rs.*;
import org.minijax.rs.persistence.*;
import org.minijax.rs.test.*;
import org.minijax.security.*;
import org.minijax.view.*;

import minitwit.Minitwit.Dao;
import minitwit.Minitwit.User;

class MinitwitTest extends MinijaxTest {
    public static User alice;
    public static Cookie aliceCookie;
    public static User bob;
    public static Cookie bobCookie;

    @BeforeAll
    public static void setUpMinitwit() throws IOException {
        getServer()
                .property("jakarta.persistence.jdbc.url", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1")
                .register(PersistenceFeature.class)
                .register(MustacheFeature.class)
                .register(new SecurityFeature(User.class, Dao.class))
                .register(Minitwit.class);

        try (final MinijaxRequestContext ctx = createRequestContext()) {
            final Dao dao = ctx.getResource(Dao.class);
            alice = new User();
            alice.setName("Alice Smith");
            alice.setHandle("alice");
            alice.setEmail("alice@example.com");
            alice.setPassword("alicepwd");
            alice.setRoles("user");
            dao.create(alice);

            aliceCookie = ctx.getResource(Security.class).loginAs(alice);

            bob = new User();
            bob.setName("Bob Johnson");
            bob.setHandle("bob");
            bob.setEmail("bob@example.com");
            bob.setPassword("bobpwd");
            bob.setRoles("bob");
            dao.create(bob);

            bobCookie = ctx.getResource(Security.class).loginAs(bob);
        }
    }

    @Test
    void testAnonymous() {
        final Response response = target("/").request().get();
        assertNotNull(response);
        assertEquals(303, response.getStatus());
        assertEquals("/public", response.getHeaderString("Location"));
    }

    @Test
    void testPublicTimeline() {
        final Response response = target("/public").request().get();
        assertNotNull(response);
        assertEquals(200, response.getStatus());

        final View view = (View) response.getEntity();
        assertNotNull(view);
        assertEquals("timeline", view.getTemplateName());
        assertNotNull(view.getModel().get("messages"));
    }

    @Test
    void testPersonalTimeline() throws Exception {
        final Response response = target("/").request().cookie(aliceCookie).get();
        assertNotNull(response);
        assertEquals(200, response.getStatus());

        final View view = (View) response.getEntity();
        assertNotNull(view);
        assertEquals("timeline", view.getTemplateName());
        assertNotNull(view.getModel().get("messages"));
    }

    @Test
    void testDifferentUserTimeline() throws Exception {
        final Response response = target("/bob").request().cookie(aliceCookie).get();
        assertNotNull(response);
        assertEquals(200, response.getStatus());

        final View view = (View) response.getEntity();
        assertNotNull(view);
        assertEquals("timeline", view.getTemplateName());
        assertNotNull(view.getModel().get("messages"));
    }

    @Test
    void testFollowUser() throws Exception {
        assertFalse(alice.following.contains(bob));

        final Response response = target("/bob/follow").request().cookie(aliceCookie).get();
        assertNotNull(response);
        assertEquals(303, response.getStatus());

        try (final MinijaxRequestContext ctx = createRequestContext()) {
            final Dao dao = ctx.getResource(Dao.class);
            assertTrue(dao.read(User.class, alice.getId()).following.contains(bob));
        }
    }

    @Test
    void testLoginPage() throws Exception {
        final View view = target("/login").request().get(View.class);
        assertNotNull(view);
        assertEquals("login", view.getTemplateName());
    }

    @Test
    void testEmptyLogin() throws Exception {
        final Form form = new Form();

        final View view = target("/login").request().post(Entity.form(form), View.class);
        assertNotNull(view);
        assertNotNull(view.getModel().get("error"));
    }

    @Test
    void testSuccessfulLogin() throws Exception {
        final Form form = new Form();
        form.param("email", "alice@example.com");
        form.param("password", "alicepwd");

        final Response response = target("/login").request().post(Entity.form(form));
        assertNotNull(response);
        assertEquals(303, response.getStatus());
        assertFalse(response.getCookies().isEmpty());
    }

    @Test
    void testRegisterPage() throws Exception {
        final View view = target("/register").request().get(View.class);
        assertNotNull(view);
        assertEquals("register", view.getTemplateName());
    }

    @Test
    void testSuccessfulRegister() throws Exception {
        final Form form = new Form();
        form.param("handle", "new_user");
        form.param("email", "new_user@example.com");
        form.param("password", "new_user_pwd");

        final Response response = target("/register").request().post(Entity.form(form));
        assertNotNull(response);
        assertEquals(303, response.getStatus());
        assertFalse(response.getCookies().isEmpty());
    }

    @Test
    void testUnauthenticatedAddMessage() throws Exception {
        final Form form = new Form();
        form.param("text", "Hello world");

        final Response response = target("/addmessage").request().post(Entity.form(form));
        assertNotNull(response);
        assertEquals(401, response.getStatus());
    }

    @Test
    void testMissingCsrfAddMessage() throws Exception {
        final Form form = new Form();
        form.param("text", "Hello world");

        final Response response = target("/addmessage").request().cookie(aliceCookie).post(Entity.form(form));
        assertNotNull(response);
        assertEquals(400, response.getStatus());
    }

    @Test
    void testIncorrectCsrfAddMessage() throws Exception {
        final Form form = new Form();
        form.param("text", "Hello world");
        form.param("csrf", "nope");

        final Response response = target("/addmessage").request().cookie(aliceCookie).post(Entity.form(form));
        assertNotNull(response);
        assertEquals(400, response.getStatus());
    }

    @Test
    void testSuccessfulAddMessage() throws Exception {
        final Form form = new Form();
        form.param("text", "Hello world");
        form.param("csrf", aliceCookie.getValue());

        final Response response = target("/addmessage").request().cookie(aliceCookie).post(Entity.form(form));
        assertNotNull(response);
        assertEquals(303, response.getStatus());
    }

    @Test
    void testGravatarUrl() throws IOException {
        final User user = new User();
        user.setEmail("cody@ebberson.com");
        assertEquals("https://www.gravatar.com/avatar/9e46d967afb5e2fd13b2802c1e64112d?d=identicon&s=80", user.gravatarUrl());
    }
}
