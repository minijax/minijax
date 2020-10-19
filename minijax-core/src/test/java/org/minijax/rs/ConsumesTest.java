package org.minijax.rs;

import static jakarta.ws.rs.core.MediaType.*;

import static org.junit.Assert.*;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.client.Entity;

import org.junit.BeforeClass;
import org.junit.Test;
import org.minijax.rs.test.MinijaxTest;

public class ConsumesTest extends MinijaxTest {

    @POST
    @Path("/consumetext")
    @Consumes(TEXT_PLAIN)
    public static String consumeText(final String text) {
        return text;
    }

    @BeforeClass
    public static void setUpConsumesTest() {
        resetServer();
        register(ConsumesTest.class);
    }

    @Test
    public void testConsumesText() {
        assertEquals(
                "Hello",
                target("/consumetext").request().post(Entity.entity("Hello", TEXT_PLAIN_TYPE), String.class));
    }
}
