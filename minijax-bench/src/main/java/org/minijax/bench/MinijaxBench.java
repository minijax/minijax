package org.minijax.bench;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.minijax.Minijax;

public class MinijaxBench {

    @GET
    @Path("/")
    public static String hello() {
        return "Hello World";
    }

    public static void main(final String[] args) {
        new Minijax().register(MinijaxBench.class).run(8080);
    }
}
