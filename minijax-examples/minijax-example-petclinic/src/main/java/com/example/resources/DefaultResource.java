package com.example.resources;

import static jakarta.ws.rs.core.MediaType.*;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import org.minijax.view.View;

@Path("/")
@Produces(TEXT_HTML)
public class DefaultResource {

    @GET
    public View getDefaultPage() {
        return new View("home");
    }
}
