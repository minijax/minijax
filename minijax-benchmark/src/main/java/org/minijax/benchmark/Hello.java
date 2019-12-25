package org.minijax.benchmark;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
public class Hello {

    @GET
    public static String hello() {
        return "Hello world!";
    }
}
