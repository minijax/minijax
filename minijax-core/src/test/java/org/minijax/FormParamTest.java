package org.minijax;

import static org.junit.Assert.*;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;

import org.junit.Test;
import org.minijax.test.MinijaxTest;

public class FormParamTest extends MinijaxTest {

    @POST
    @Path("/formtest")
    public static String formPassThrough(@FormParam("test") final String test) {
        return test;
    }

    @Test
    public void testFormParam() {
        register(FormParamTest.class);

        final Entity<Form> form = Entity.form(new Form("test", "Hello"));

        assertEquals(
                "Hello",
                target("/formtest").request().post(form, String.class));
    }
}
