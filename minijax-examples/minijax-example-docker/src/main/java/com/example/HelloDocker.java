package com.example;

import javax.ws.rs.*;

@Path("/")
public class HelloDocker {

    @GET
    public static String hello() {
        return "Hello world!";
    }

    public static void main(String[] args) {
        new org.minijax.Minijax()
                .register(HelloDocker.class)
                .start();
    }
}
