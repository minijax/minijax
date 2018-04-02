package com.example;

import javax.ws.rs.*;

@Path("/")
public class Hello {

    @GET
    public static String hello() {
        return "Hello world!";
    }

    public static void main(String[] args) {
        new org.minijax.Minijax()
                .register(Hello.class)
                .start();
    }
}
