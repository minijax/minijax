package minitwit;

import static org.junit.Assert.*;

import javax.ws.rs.core.Response;

import org.junit.*;
import org.minijax.mustache.MustacheFeature;
import org.minijax.security.*;
import org.minijax.test.MinijaxTest;
import org.minijax.view.*;

import minitwit.Minitwit.Dao;
import minitwit.Minitwit.User;

public class MinitwitTest extends MinijaxTest {

    @BeforeClass
    public static void setUpMinitwit() {
        getServer()
                .property("javax.persistence.jdbc.url", "jdbc:h2:mem:test")
                .registerPersistence()
                .register(MustacheFeature.class)
                .register(new SecurityFeature(User.class))
                .register(Dao.class, SecurityDao.class)
                .register(Minitwit.class);
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
}
