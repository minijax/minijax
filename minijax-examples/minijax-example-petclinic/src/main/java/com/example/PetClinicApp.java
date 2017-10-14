package com.example;

import javax.persistence.Persistence;

import org.minijax.Minijax;
import org.minijax.mustache.MinijaxMustacheFeature;

import com.example.services.Dao;

public class PetClinicApp {

    public static void main(final String[] args) {
        new Minijax()
                .register(new Dao(Persistence.createEntityManagerFactory("petclinic")))
                .register(MinijaxMustacheFeature.class)
                .packages("com.example")
                .run(8081);
    }
}
