package org.minijax;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.Part;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
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
import org.minijax.util.IOUtils;

public class FormParamTest extends MinijaxTest {

    @POST
    @Path("/formtest")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public static String formPassThrough(@FormParam("test") final String test) {
        return test;
    }

    @POST
    @Path("/defval")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public static String getDefaultValue(@FormParam("test") @DefaultValue("foo") final String test) {
        return test;
    }

    @POST
    @Path("/wholeform")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public static String getWholeForm(@Context final Form form) {
        return form.asMap().getFirst("test");
    }

    @POST
    @Path("/multipart-form")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public static String getMultipartForm(@Context final Form form) {
        return form.asMap().getFirst("key");
    }

    @POST
    @Path("/multipart-minijaxform")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public static String getMultipartMinijaxForm(@Context final MinijaxForm form) {
        return form.getString("key");
    }

    @POST
    @Path("/multipart-params")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public static String getMultipartParams(
            @FormParam("key") final String key,
            @FormParam("content") final InputStream content)
                    throws IOException {
        return key + "-" + IOUtils.toString(content, StandardCharsets.UTF_8);
    }

    @POST
    @Path("/multipart-optional")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public static String getMultipartOptionalInputStream(
            @FormParam("content") final InputStream content)
                    throws IOException {
        return content == null ? "null" : IOUtils.toString(content, StandardCharsets.UTF_8);
    }

    @POST
    @Path("/multipart-part")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public static String getMultipartPart(
            @FormParam("content") final Part content)
                    throws IOException {
        return content.getSubmittedFileName();
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
    public void testFormParamDefaultValue() {
        final Entity<Form> form = Entity.form(new Form());
        assertEquals(
                "foo",
                target("/defval").request().post(form, String.class));
    }

    @Test
    public void testWholeForm() {
        final Entity<Form> form = Entity.form(new Form("test", "Hello"));
        assertEquals(
                "Hello",
                target("/wholeform").request().post(form, String.class));
    }

    @Test
    public void testMultipartForm() {
        final String mockContent =
                "------WebKitFormBoundarycTqA2AimXQHBAJbZ\n" +
                "Content-Disposition: form-data; name=\"key\"\n" +
                "\n" +
                "myvalue1\n" +
                "------WebKitFormBoundarycTqA2AimXQHBAJbZ";

        final Entity<String> entity = Entity.entity(mockContent, MediaType.MULTIPART_FORM_DATA_TYPE);

        assertEquals("myvalue1", target("/multipart-form").request().post(entity, String.class));
    }

    @Test
    public void testMultipartMinijaxForm() {
        final String mockContent =
                "------WebKitFormBoundarycTqA2AimXQHBAJbZ\n" +
                "Content-Disposition: form-data; name=\"key\"\n" +
                "\n" +
                "myvalue1\n" +
                "------WebKitFormBoundarycTqA2AimXQHBAJbZ";

        final Entity<String> entity = Entity.entity(mockContent, MediaType.MULTIPART_FORM_DATA_TYPE);

        assertEquals("myvalue1", target("/multipart-minijaxform").request().post(entity, String.class));
    }

    @Test
    public void testMultipartParams() {
        final String mockContent =
                "------WebKitFormBoundarycTqA2AimXQHBAJbZ\n" +
                "Content-Disposition: form-data; name=\"key\"\n" +
                "\n" +
                "myvalue1\n" +
                "------WebKitFormBoundarycTqA2AimXQHBAJbZ\n" +
                "Content-Disposition: form-data; name=\"content\"; filename=\"hello.txt\"\n" +
                "Content-Type: text/plain\n" +
                "\n" +
                "Hello world\n" +
                "------WebKitFormBoundarycTqA2AimXQHBAJbZ";

        final Entity<String> entity = Entity.entity(mockContent, MediaType.MULTIPART_FORM_DATA_TYPE);

        assertEquals("myvalue1-Hello world", target("/multipart-params").request().post(entity, String.class));
    }

    @Test
    public void testMultipartOptionalWithFile() {
        final String mockContent =
                "------WebKitFormBoundarycTqA2AimXQHBAJbZ\n" +
                "Content-Disposition: form-data; name=\"content\"; filename=\"hello.txt\"\n" +
                "Content-Type: text/plain\n" +
                "\n" +
                "Hello world\n" +
                "------WebKitFormBoundarycTqA2AimXQHBAJbZ";

        final Entity<String> entity = Entity.entity(mockContent, MediaType.MULTIPART_FORM_DATA_TYPE);

        assertEquals("Hello world", target("/multipart-optional").request().post(entity, String.class));
    }

    @Test
    public void testMultipartOptionalWithoutFile() {
        final String mockContent =
                "------WebKitFormBoundarycTqA2AimXQHBAJbZ\n" +
                "------WebKitFormBoundarycTqA2AimXQHBAJbZ";

        final Entity<String> entity = Entity.entity(mockContent, MediaType.MULTIPART_FORM_DATA_TYPE);

        assertEquals("null", target("/multipart-optional").request().post(entity, String.class));
    }

    @Test
    public void testMultipartPart() {
        final String mockContent =
                "------WebKitFormBoundarycTqA2AimXQHBAJbZ\n" +
                "Content-Disposition: form-data; name=\"content\"; filename=\"hello.txt\"\n" +
                "Content-Type: text/plain\n" +
                "\n" +
                "Hello world\n" +
                "------WebKitFormBoundarycTqA2AimXQHBAJbZ";

        final Entity<String> entity = Entity.entity(mockContent, MediaType.MULTIPART_FORM_DATA_TYPE);

        assertEquals("hello.txt", target("/multipart-part").request().post(entity, String.class));
    }
}
