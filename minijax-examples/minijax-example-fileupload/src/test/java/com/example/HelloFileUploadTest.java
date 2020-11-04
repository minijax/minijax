package com.example;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.minijax.mustache.MustacheFeature;
import org.minijax.rs.multipart.Multipart;
import org.minijax.rs.test.MinijaxTest;
import org.minijax.view.View;

class HelloFileUploadTest extends MinijaxTest {

    @BeforeEach
    public void setUp() {
        register(MustacheFeature.class);
        register(HelloFileUpload.class);
    }

    @Test
    void testHome() {
        final Response response = target("/").request().get();
        assertNotNull(response);
        assertEquals(200, response.getStatus());

        final View view = (View) response.getEntity();
        assertNotNull(view);
        assertEquals("home", view.getTemplateName());

        final String str = response.readEntity(String.class);
        assertNotNull(str);
        assertEquals(
                "<html>\n"
                + "  <body>\n"
                + "    <p>Upload a file</p>\n"
                + "    <form method=\"post\" enctype=\"multipart/form-data\">\n"
                + "      <input type=\"file\" name=\"file1\">\n"
                + "      <input type=\"submit\" value=\"Upload\">\n"
                + "    </form>\n"
                + "  </body>\n"
                + "</html>\n",
                str);
    }

    @Test
    void testUpload() throws IOException {
        try (final Multipart form = new Multipart()) {
            form.param("file1", "testfile.txt", IOUtils.toInputStream("Hello world!"));

            final Response response = target("/").request().post(Entity.entity(form, form.getContentType()));
            assertNotNull(response);
            assertEquals(200, response.getStatus());

            final View view = (View) response.getEntity();
            assertNotNull(view);
            assertEquals("upload", view.getTemplateName());

            final String str = response.readEntity(String.class);
            assertNotNull(str);
            assertEquals(
                    "<html>\n"
                    + "  <body>\n"
                    + "    <p>Name: testfile.txt</p>\n"
                    + "    <p>Contents:</p>\n"
                    + "    <pre>Hello world!</pre>\n"
                    + "  </body>\n"
                    + "</html>\n",
                    str);
        }
    }
}
