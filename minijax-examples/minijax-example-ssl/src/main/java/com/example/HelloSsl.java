package com.example;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import org.minijax.Minijax;

public class HelloSsl {

    @GET
    @Path("/")
    public static String hello() {
        return "Hello world!";
    }

    public static void main(final String[] args) {
        new Minijax()
                .register(HelloSsl.class)
                .secure("keystore.jks", "certpassword", "certpassword")
                .start();
    }
}
