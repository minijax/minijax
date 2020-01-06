package org.minijax.rs;

import static javax.ws.rs.core.MediaType.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedMap;

import org.junit.Before;
import org.junit.Test;
import org.minijax.Minijax;
import org.minijax.commons.IOUtils;
import org.minijax.rs.multipart.Multipart;
import org.minijax.rs.multipart.Part;

public class FormParamTest {

    @POST
    @Path("/formtest")
    @Consumes(APPLICATION_FORM_URLENCODED)
    public static String formPassThrough(@FormParam("test") final String test) {
        return test;
    }

    @POST
    @Path("/defval")
    @Consumes(APPLICATION_FORM_URLENCODED)
    public static String getDefaultValue(@FormParam("test") @DefaultValue("foo") final String test) {
        return test;
    }

    @POST
    @Path("/wholeform")
    @Consumes(APPLICATION_FORM_URLENCODED)
    public static String getWholeForm(final MultivaluedMap<String, String> form) {
        return form.getFirst("test");
    }

    @POST
    @Path("/multipart-form")
    @Consumes(MULTIPART_FORM_DATA)
    public static String getMultipartForm(final MultivaluedMap<String, String> form) {
        return form.getFirst("key");
    }

    @POST
    @Path("/multipart-params")
    @Consumes(MULTIPART_FORM_DATA)
    public static String getMultipartParams(
            @FormParam("key") final String key,
            @FormParam("content") final InputStream content)
                    throws IOException {
        return key + "-" + IOUtils.toString(content, StandardCharsets.UTF_8);
    }

    @POST
    @Path("/multipart-optional")
    @Consumes(MULTIPART_FORM_DATA)
    public static String getMultipartOptionalInputStream(
            @FormParam("content") final InputStream content)
                    throws IOException {
        return content == null ? "null" : IOUtils.toString(content, StandardCharsets.UTF_8);
    }

    @POST
    @Path("/multipart-part")
    @Consumes(MULTIPART_FORM_DATA)
    public static String getMultipartPart(
            @FormParam("content") final Part content)
                    throws IOException {
        return content.getSubmittedFileName();
    }

    private Minijax server;

    @Before
    public void setUp() {
        server = new Minijax().register(FormParamTest.class);
    }

    @Test
    public void testFormParam() {
        final Entity<Form> form = Entity.form(new Form("test", "Hello"));
        assertEquals(
                "Hello",
                server.target("/formtest").request().post(form, String.class));
    }

    @Test
    public void testFormParamDefaultValue() {
        final Entity<Form> form = Entity.form(new Form());
        assertEquals(
                "foo",
                server.target("/defval").request().post(form, String.class));
    }

    @Test
    public void testWholeForm() {
        final Entity<Form> form = Entity.form(new Form("test", "Hello"));
        assertEquals(
                "Hello",
                server.target("/wholeform").request().post(form, String.class));
    }

    @Test
    public void testMultipartForm() throws IOException {
        try (final Multipart multipart = new Multipart()) {
            multipart.param("key", "myvalue1");

            final Entity<Multipart> entity = Entity.entity(multipart, multipart.getContentType());
            assertEquals("myvalue1", server.target("/multipart-form").request().post(entity, String.class));
        }
    }

    @Test
    public void testMultipartParams() throws IOException {
        try (final Multipart multipart = new Multipart()) {
            multipart.param("key", "myvalue1");
            multipart.param("content", "Hello world\n");

            final Entity<Multipart> entity = Entity.entity(multipart, multipart.getContentType());
            assertEquals("myvalue1", server.target("/multipart-form").request().post(entity, String.class));
        }
    }

    @Test
    public void testMultipartOptionalWithFile() throws IOException {
        try (final Multipart multipart = new Multipart()) {
            multipart.param("key", "myvalue1");
            multipart.param("content", "Hello world");

            final Entity<Multipart> entity = Entity.entity(multipart, multipart.getContentType());
            assertEquals("Hello world", server.target("/multipart-optional").request().post(entity, String.class));
        }
    }

    @Test
    public void testMultipartOptionalWithoutFile() throws IOException {
        try (final Multipart multipart = new Multipart()) {
            final Entity<Multipart> entity = Entity.entity(multipart, multipart.getContentType());
            assertEquals("null", server.target("/multipart-optional").request().post(entity, String.class));
        }
    }
}