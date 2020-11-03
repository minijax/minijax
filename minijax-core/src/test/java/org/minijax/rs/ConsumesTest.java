package org.minijax.rs;

import static org.junit.jupiter.api.Assertions.*;

import static jakarta.ws.rs.core.MediaType.*;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.client.Entity;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.minijax.rs.test.MinijaxTest;

public class ConsumesTest extends MinijaxTest {

    @POST
    @Path("/consumetext")
    @Consumes(TEXT_PLAIN)
    public static String consumeText(final String text) {
        return text;
    }

    @BeforeAll
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
