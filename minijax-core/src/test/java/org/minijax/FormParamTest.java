package org.minijax;

import static org.junit.Assert.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import org.junit.Before;
import org.junit.Test;
import org.minijax.test.MinijaxTest;

public class FormParamTest extends MinijaxTest {

    @POST
    @Path("/formtest")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public static String formPassThrough(@FormParam("test") final String test) {
        return test;
    }

    @POST
    @Path("/wholeform")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public static String getWholeForm(@Context final Form form) {
        return form.asMap().getFirst("test");
    }

    @Before
    public void setUp() {
        register(FormParamTest.class);
    }

    @Test
    public void testFormParam() {
        final Entity<Form> form = Entity.form(new Form("test", "Hello"));
        assertEquals(
                "Hello",
                target("/formtest").request().post(form, String.class));
    }

    @Test
    public void testWholeForm() {
        final Entity<Form> form = Entity.form(new Form("test", "Hello"));
        assertEquals(
                "Hello",
                target("/wholeform").request().post(form, String.class));
    }
}
