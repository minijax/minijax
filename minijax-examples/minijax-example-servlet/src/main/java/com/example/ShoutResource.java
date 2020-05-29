package com.example;

import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

@Path("/shout")
public class ShoutResource {

    @GET
    public String shout(@QueryParam("name") @DefaultValue("friend") final String name) {
        return ("Hello " + name + "!").toUpperCase();
    }
}
