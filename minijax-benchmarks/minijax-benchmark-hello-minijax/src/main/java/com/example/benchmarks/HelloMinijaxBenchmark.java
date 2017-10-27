package com.example.benchmarks;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.minijax.Minijax;

public class HelloMinijaxBenchmark {

    @GET
    @Path("/")
    public static String hello() {
        return "Hello World";
    }

    public static void main(final String[] args) {
        new Minijax().register(HelloMinijaxBenchmark.class).run(8080);
    }
}
