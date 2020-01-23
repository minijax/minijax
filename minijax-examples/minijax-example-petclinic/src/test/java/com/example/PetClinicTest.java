package com.example;

import org.minijax.mustache.MustacheFeature;
import org.minijax.rs.persistence.PersistenceFeature;
import org.minijax.rs.test.MinijaxTest;

public abstract class PetClinicTest extends MinijaxTest {
    protected PetClinicTest() {
        getServer()
            .property("javax.persistence.jdbc.url", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1")
            .register(PersistenceFeature.class)
            .register(MustacheFeature.class);
    }
}
