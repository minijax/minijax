package com.example.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.minijax.mustache.View;

public class DefaultResource {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public View getDefaultPage() {
        return new View("home");
    }
}
