package org.minijax.benchmark;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/")
public class Hello {

    @GET
    public static String hello() {
        return "Hello world!";
    }
}
