package com.example;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("/shout")
public class ShoutResource {

    @GET
    public String shout(@QueryParam("name") @DefaultValue("friend") final String name) {
        return ("Hello " + name + "!").toUpperCase();
    }
}
