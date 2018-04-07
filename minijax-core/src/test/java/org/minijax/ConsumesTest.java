package org.minijax;

import static javax.ws.rs.core.MediaType.*;

import static org.junit.Assert.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.client.Entity;

import org.junit.BeforeClass;
import org.junit.Test;
import org.minijax.test.MinijaxTest;

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
