package com.example.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.minijax.mustache.View;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class DefaultResource {

    @GET
    public View getDefaultPage() {
        return new View("home");
    }
}
