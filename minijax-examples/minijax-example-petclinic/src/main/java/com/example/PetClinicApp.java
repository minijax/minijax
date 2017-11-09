package com.example;

import org.minijax.Minijax;
import org.minijax.MinijaxProperties;
import org.minijax.mustache.MinijaxMustacheFeature;

class PetClinicApp {

    public static void main(final String[] args) {
        new Minijax()
                .property(MinijaxProperties.DB_DRIVER, "org.h2.Driver")
                .property(MinijaxProperties.DB_URL, "jdbc:h2:~/.minijax-petclinic/database")
                .registerPersistence()
                .register(MinijaxMustacheFeature.class)
                .addStaticDirectory("static")
                .packages("com.example")
                .run(8080);
    }
}
