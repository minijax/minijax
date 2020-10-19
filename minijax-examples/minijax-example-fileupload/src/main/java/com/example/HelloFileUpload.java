package com.example;

import static jakarta.ws.rs.core.MediaType.*;

import java.util.HashMap;
import java.util.Map;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import org.minijax.Minijax;
import org.minijax.mustache.MustacheFeature;
import org.minijax.rs.multipart.Part;
import org.minijax.view.View;

@Path("/")
@Produces(TEXT_HTML)
public class HelloFileUpload {

    @GET
    public static View home() {
        return new View("home");
    }

    @POST
    @Consumes(MULTIPART_FORM_DATA)
    public static View upload(@FormParam("file1") final Part file) {
        final Map<String, Object> model = new HashMap<>();
        model.put("name", file.getSubmittedFileName());
        model.put("value", file.getValue());

        return new View("upload", model);
    }

    public static void main(final String[] args) {
        new Minijax()
                .register(MustacheFeature.class)
                .register(HelloFileUpload.class)
                .start();
    }
}
