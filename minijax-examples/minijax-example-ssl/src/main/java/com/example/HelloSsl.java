package com.example;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

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
                .start(8080);
    }
}
