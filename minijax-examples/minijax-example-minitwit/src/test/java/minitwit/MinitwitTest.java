package minitwit;

import static org.junit.Assert.*;

import javax.ws.rs.core.Response;

import org.junit.*;
import org.minijax.MinijaxProperties;
import org.minijax.mustache.MinijaxMustacheFeature;
import org.minijax.test.MinijaxTest;

public class MinitwitTest extends MinijaxTest {

    @BeforeClass
    public static void setUpMinitwit() {
        getServer()
            .property(MinijaxProperties.DB_DRIVER, "org.h2.Driver")
            .property(MinijaxProperties.DB_URL, "jdbc:h2:mem:test")
            .registerPersistence()
            .register(MinijaxMustacheFeature.class)
            .register(Minitwit.class);
    }

    @Test
    @Ignore("Need sessions")
    public void testAnonymous() {
        final Response response = target("/").request().get();
        assertNotNull(response);
        assertEquals(303, response.getStatus());
        assertEquals("/public", response.getHeaderString("Location"));
    }
}
