package org.minijax;

import static org.junit.Assert.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.junit.Test;
import org.minijax.test.MinijaxTest;

public class ConsumesTest extends MinijaxTest {

    @POST
    @Path("/consumetext")
    @Consumes(MediaType.TEXT_PLAIN)
    public static String consumeText(final String text) {
        return text;
    }

    @Test
    public void testConsumesText() {
        register(ConsumesTest.class);

        assertEquals(
                "Hello",
                target("/consumetext").request().post(Entity.entity("Hello", MediaType.TEXT_PLAIN_TYPE), String.class));
    }
}
