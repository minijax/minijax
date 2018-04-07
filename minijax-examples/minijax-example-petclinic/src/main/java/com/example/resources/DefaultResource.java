package com.example.resources;

import static javax.ws.rs.core.MediaType.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.minijax.view.View;

@Path("/")
@Produces(TEXT_HTML)
public class DefaultResource {

    @GET
    public View getDefaultPage() {
        return new View("home");
    }
}
